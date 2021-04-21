package SELab.service;

import SELab.domain.Article;
import SELab.domain.Author;
import SELab.domain.Meeting;
import SELab.domain.User;
import SELab.exception.InternalServerError;
import SELab.exception.MeetingOfNoExistenceException;
import SELab.exception.MeetingUnavaliableToOperateException;
import SELab.exception.UserNamedidntExistException;
import SELab.exception.user.ArticleNotFoundException;
import SELab.repository.ArticleRepository;
import SELab.request.user.ArticleRequest;
import SELab.service.client.MeetingClient;
import SELab.service.client.UserClient;
import SELab.utility.contract.ArticleStatus;
import SELab.utility.contract.MeetingStatus;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author qym
 * @date 2020/11/3
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleRepository articleRepository;
    Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    MeetingClient meetingClient;

    @Autowired
    UserClient userClient;

    @Transactional
    public ResponseWrapper<?> authorMeeting(String username) {
        List<Article> articleList = articleRepository.findByContributorName(username);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        Set<Long> meetingCount = new HashSet<>();
        for (Article article : articleList) {
            Meeting meeting = meetingClient.findByMeetingName(article.getMeetingname());
            if (meeting != null && !meetingCount.contains(meeting.getId())) {
                meetingCount.add(meeting.getId());
                response.add(ResponseGenerator.generate(meeting,
                        new String[]{"meetingName", "acronym", "submissionDeadlineDate", "topic"}, null));
            }
        }
        body.put("meetings", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Transactional
    public ResponseWrapper<?> getArticleDetail(String articleId) {
        Article article = articleRepository.findById(Long.parseLong(articleId)); // 不能为空，否则有空指针错误
        if (article == null) {
            throw new ArticleNotFoundException(articleId);
        }
        HashMap<String, Object> returnMap = ResponseGenerator.generate(
                article,
                new String[]{"contributorName", "meetingName", "submitDate",
                        "title", "articleAbstract", "filePath", "status"}, null
        );
        if (returnMap == null)
            throw new InternalServerError("in Article Service, in getArticleDetail");
        Set<Author> returnAuthors = new HashSet<>();
        for (Pair<Author, Integer> p : article.getAuthors()) {
            returnAuthors.add(p.getKey());
        }
        returnMap.put("authors", returnAuthors);
        returnMap.put("topic", article.getTopic());

        HashMap<String, HashMap<String, Object>> body = new HashMap<>();
        body.put("articleDetail", returnMap);

        return new ResponseWrapper<>(200, ResponseGenerator.success, body);

    }


    @Transactional
    public ResponseWrapper<?> submitNewArticle(ArticleRequest request, String targetRootDir) {
        String meetingName = request.getMeetingName();
        String username = request.getUsername();

        Meeting meeting = meetingClient.findByMeetingName(meetingName);
        User articleUploader = userClient.findByUsername(username);

        //guarantee that this operation is valid
        authenticateArticle(meeting, articleUploader);

        MultipartFile pdfFile = request.getFile();
        //save the file, if exceptions happens, throw new InternalServerError
        String internalFilePath = targetRootDir +
                articleUploader.getUsername() + File.separator +
                request.getSubmitDate() + File.separator;
        try {
            saveFileToServer(pdfFile, internalFilePath);
        } catch (IOException ex) {
            throw new InternalServerError("UserArticleService.uploadNewArticle(): error occurred when saving the article pdf");
        }

        Set<String> clearTopics = getClearedTopics(request.getTopics());

        Article newArticle = new Article(
                request.getUsername(),
                request.getMeetingName(),
                request.getSubmitDate(),
                request.getEssayTitle(),
                request.getEssayAbstract(),
                internalFilePath + pdfFile.getOriginalFilename(),
                ArticleStatus.queuing,
                clearTopics,
                request.getAuthors()
        );
        articleRepository.save(newArticle);

        return new ResponseWrapper<>(200, ResponseGenerator.success, new HashMap<>());
    }

    @Transactional
    public ResponseWrapper<?> updateArticle(String articleId, ArticleRequest request, String targetRootDir) {
        Article article = articleRepository.findById(Long.parseLong(articleId));
        if (article == null)
            throw new ArticleNotFoundException(articleId);

        Meeting meeting = meetingClient.findByMeetingName(request.getMeetingName());
        User user = userClient.findByUsername(request.getUsername());

        authenticateArticle(meeting, user);
        if (request.getFile() != null) {

            //delete the previous pdf file
            String previousPdfPath = article.getFilePath();
            File file = new File(previousPdfPath);
            if (file.exists()) {
                if (!file.delete())
                    throw new InternalServerError("UserArticleService.updateExistedArticle(): file delete failed");
            } else {
                throw new InternalServerError("UserArticleService.updateExistedArticle(): previous pdf doesn't exist");
            }
            String internalFilePath = targetRootDir +
                    user.getUsername() + File.separator +
                    request.getSubmitDate() + File.separator;

            MultipartFile pdfFile = request.getFile();

            try {
                saveFileToServer(pdfFile, internalFilePath);
            } catch (IOException ex) {
                throw new InternalServerError("UserArticleService.uploadNewArticle(): error occurred when saving the article pdf");
            }
            article.setFilePath(internalFilePath + pdfFile.getOriginalFilename());
        }
        Set<String> clearTopics = getClearedTopics(request.getTopics());


        //then update all the information of the previous article
        article.setMeetingname(request.getMeetingName());
        article.setContributorName(request.getUsername());
        article.setTitle(request.getEssayTitle());
        article.setArticleAbstract(request.getEssayAbstract());
        article.setSubmitDate(request.getSubmitDate());

        article.setTopic(clearTopics);
        article.setAuthors(request.getAuthors());

        articleRepository.save(article);
        //after all the update, return the success message

        return new ResponseWrapper<>(200, ResponseGenerator.success, new HashMap<>());
    }

    @Transactional
    public ResponseWrapper<?> findById(long id) {
        Article article = articleRepository.findById(id);
        if (article == null) {
            throw new ArticleNotFoundException("article does not exist:" + id);
        }
        HashMap<String, Object> returnMap = ResponseGenerator.generate(
                article,
                new String[]{"contributorName", "meetingName", "submitDate",
                        "title", "articleAbstract", "filePath", "status"}, null
        );
        if (returnMap == null)
            throw new InternalServerError("in Article Service, in getArticleDetail");
        Set<Author> returnAuthors = new HashSet<>();
        for (Pair<Author, Integer> p : article.getAuthors()) {
            returnAuthors.add(p.getKey());
        }
        returnMap.put("authors", returnAuthors);
        returnMap.put("topic", article.getTopic());

        HashMap<String, HashMap<String, Object>> body = new HashMap<>();
        body.put("articleDetail", returnMap);

        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Transactional
    public ResponseWrapper<?> getSubmissionList(String authorName, String meetingName) {
        Meeting meeting = meetingClient.findByMeetingName(meetingName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }//会议是否存在
        List<Article> articles = articleRepository.findByContributorNameAndMeetingName(authorName, meetingName);
        return ResponseGenerator.injectObjectFromListToResponse("articles", articles, new String[]{"id", "title", "topic", "status"}, null);
    }

    @Transactional
    public ResponseWrapper<?> findByMeetingNameAndStatus(String meetingName, String status) {
        Meeting meeting = meetingClient.findByMeetingName(meetingName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }//会议是否存在
        List<Article> articles = articleRepository.findByMeetingNameAndStatus(meetingName, status);
        return ResponseGenerator.injectObjectFromListToResponse("articles", articles, new String[]{"id", "title", "topic", "status", "authors"}, null);
    }

    //this function is used to guarantee a article can be upload or update
    private void authenticateArticle(Meeting meeting, User user) {
        if (meeting == null)
            throw new MeetingUnavaliableToOperateException("Not created");
        if (user == null)
            throw new UserNamedidntExistException("not a valid user");

        if (!meeting.getStatus().equals(MeetingStatus.submissionAvaliable))
            throw new MeetingUnavaliableToOperateException("update or upload articles");
    }


    //Before use this internal method
    //please Guarantee that file is savable (not null)
    private void saveFileToServer(MultipartFile file, String rootDirPath)
            throws IOException {

        byte[] fileBytes = file.getBytes();
        Path restorePath = Paths.get(rootDirPath + file.getOriginalFilename());

        //如果没有rootDirPath文件夹，则创建
        if (!Files.isWritable(restorePath)) {
            Files.createDirectories(Paths.get(rootDirPath));
        }

        Files.write(restorePath, fileBytes);
    }


    private Set<String> getClearedTopics(Set<String> topic) {
        Set<String> clearTopics = new HashSet<>();

        for (String t : topic) {
            t = t.replaceAll("\"", "");
            t = t.replace("[", "");
            t = t.replace("]", "");
            clearTopics.add(t);
        }

        return clearTopics;

    }

    public ResponseWrapper<?> save(Article article) {
        article.setMeetingname(article.getArticleAbstract());
        articleRepository.save(article);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    @Override
    public ResponseWrapper<?> findArticleMessagesByChairName(String chairName) {
        ArrayList<String> messages = new ArrayList<>();
        List<Meeting> meetings = meetingClient.findByChairName(chairName);
        for (Meeting meeting : meetings) {
            List<Article> articles = articleRepository.findByMeetingName(meeting.getMeetingName());
            for (Article article : articles) {
                messages.add("Article named " + article.getTitle() + " has been submitted in the " + meeting.getMeetingName() + " meeting and its abstract : " + article.getArticleAbstract());
            }
        }
        HashMap<String, ArrayList<String>> body = new HashMap<>();
        body.put("messages", messages);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Override
    public ResponseWrapper<?> findArticleResultMessagesByAuthorName(String authorName) {
        ArrayList<String> messages = new ArrayList<>();
        List<Article> articles = articleRepository.findArticlesByStatusNot(ArticleStatus.queuing);
        for (Article article : articles) {
            Set<Pair<Author, Integer>> authors = article.getAuthors();
            for (Pair authorPair : authors) {
                Author author = (Author) authorPair.getKey();
                if (author.getFullname().equals(authorName)) {
                    messages.add("Your article named " + article.getTitle() + " submitted in the " + article.getMeetingname() + " meeting has been " + article.getStatus());
                    break;
                }
            }
        }
        HashMap<String, ArrayList<String>> body = new HashMap<>();
        body.put("messages", messages);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }
}
