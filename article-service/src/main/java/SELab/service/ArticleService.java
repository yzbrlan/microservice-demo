package SELab.service;

import SELab.domain.Article;
import SELab.request.user.ArticleRequest;
import SELab.utility.response.ResponseWrapper;

/**
 * @author qym
 * @date 2020/11/3
 */
public interface ArticleService {
    ResponseWrapper<?> authorMeeting(String username);

    ResponseWrapper<?> getArticleDetail(String articleId);

    ResponseWrapper<?> submitNewArticle(ArticleRequest request, String targetRootDir);

    ResponseWrapper<?> updateArticle(String articleId, ArticleRequest request, String targetRootDir);

    ResponseWrapper<?> findById(long id);

    ResponseWrapper<?> getSubmissionList(String authorName, String meetingName);

    ResponseWrapper<?> findByMeetingNameAndStatus(String meetingName, String status);

    ResponseWrapper<?> save(Article article);

    ResponseWrapper<?> findArticleMessagesByChairName(String chairName);

    ResponseWrapper<?> findArticleResultMessagesByAuthorName(String authorName);
}
