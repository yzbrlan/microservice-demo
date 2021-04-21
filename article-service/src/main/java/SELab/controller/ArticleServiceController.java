package SELab.controller;

import SELab.domain.Article;
import SELab.domain.Author;
import SELab.request.user.ArticleRequest;
import SELab.service.ArticleService;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author qym
 * @date 2020/11/3
 */
@RestController
public class ArticleServiceController {

    @Autowired
    ArticleService service;

    Logger logger = LoggerFactory.getLogger(ArticleServiceController.class);

    @GetMapping("/user/authorMeeting")
    public ResponseEntity<?> authorMeeting(String username) {
        logger.debug("Get author meeting info : " + username);
        return ResponseEntity.ok(service.authorMeeting(username));
    }


    //get the detailed information about a article
    @GetMapping("/user/articleDetail")
    public ResponseEntity<?> getArticleDetail(String articleId) {
        logger.debug("article detail get request received, article id = " + articleId);
        return ResponseEntity.ok(service.getArticleDetail(articleId));
    }


    //user submit a new article for a meeting
    @PostMapping("/user/articleSubmission")
    public ResponseEntity<?> submitNewArticle(
            @RequestParam("meetingName") String meetingName,
            @RequestParam("username") String username,
            @RequestParam("essayTitle") String essayTitle,
            @RequestParam("essayAbstract") String essayAbstract,
            @RequestParam("submitTime") String submitTime,
            @RequestParam("topic") Set<String> topic,
            @RequestParam("authors") String authors,
            @RequestParam("essayPDF") MultipartFile pdfFile,
            HttpServletRequest servletRequest

    ) {

        Set<Pair<Author, Integer>> authorArgument = generateAuthor(authors);

        ArticleRequest request = new ArticleRequest(
                meetingName, username, essayTitle, essayAbstract,
                submitTime, pdfFile, topic, authorArgument
        );
        String parentDir = servletRequest.getServletContext().getRealPath("src/resources/");
        return ResponseEntity.ok(service.submitNewArticle(request, parentDir));
    }

    //user update an existing paper
    @PostMapping("/user/updateArticle")
    public ResponseEntity<?> updateArticle(
            @RequestParam("articleId") String articleId,
            @RequestParam("meetingName") String meetingName,
            @RequestParam("username") String username,
            @RequestParam("essayTitle") String essayTitle,
            @RequestParam("essayAbstract") String essayAbstract,
            @RequestParam("submitTime") String submitTime,
            @RequestParam("topic") Set<String> topic,
            @RequestParam("authors") String authors,
            @RequestParam(value = "essayPDF", required = false) MultipartFile pdfFile,
            HttpServletRequest servletRequest

    ) {
        Set<Pair<Author, Integer>> authorArgument = generateAuthor(authors);

        ArticleRequest request = new ArticleRequest(
                meetingName, username, essayTitle, essayAbstract,
                submitTime, pdfFile, topic, authorArgument
        );


        String parentDir = servletRequest.getServletContext().getRealPath("src/resources/static/");
        return ResponseEntity.ok(service.updateArticle(articleId, request, parentDir));
    }

    @GetMapping("/article/findById")
    public ResponseEntity<?> findById(long id) {
        logger.debug("Find article by id " + id + " : ");
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/meeting/submissionList")
    public ResponseEntity<?> getSubmissionList(String authorName, String meetingName) {
        logger.debug("Submission List");
        return ResponseEntity.ok(service.getSubmissionList(authorName, meetingName));
    }

    @GetMapping("/article/findByMeetingNameAndStatus")
    public ResponseEntity<?> findByMeetingNameAndStatus(String meetingName, String status) {
        logger.debug("Find article by meeting name and status " + meetingName + ": " + "status " + status + " : ");
        return ResponseEntity.ok(service.findByMeetingNameAndStatus(meetingName, status));
    }

    @PostMapping("/article/save")
    public ResponseEntity<?> save(@RequestBody Article article) {
        return ResponseEntity.ok(service.save(article));
    }


    @GetMapping("/getArticleMessagesByChairName")
    public ResponseEntity<?> getArticleMessagesByChairName(String chairName) {
        return ResponseEntity.ok(service.findArticleMessagesByChairName(chairName));
    }

    @GetMapping("/getArticleResultMessagesByAuthorName")
    public ResponseEntity<?> getArticleResultMessagesByAuthorName(String authorName) {
        return ResponseEntity.ok(service.findArticleResultMessagesByAuthorName(authorName));
    }

    private Set<Pair<Author, Integer>> generateAuthor(String authors) {
        List<Author> authorsList = JSONArray.parseArray(authors, Author.class);
        Set<Pair<Author, Integer>> authorArgument = new HashSet<>();
        int rank = 1;
        for (Author author: authorsList){
            authorArgument.add(Pair.of(author, rank++));
        }
        return authorArgument;
    }


}
