package SELab.service.client;

import SELab.domain.Article;

import java.util.List;

/**
 * @author xywu
 * @date 2020/11/03
 */
public interface ArticleClient {

    void save(Article article);
    Article findById(long id);
    List<Article> findByIdNot(long id);
    List<Article> findByContributorName(String contributorName);
    List<Article> findByMeetingName(String meetingName);
    List<Article> findByMeetingNameAndStatus(String meetingName,String status);
    List<Article> findByContributorNameAndMeetingName(String contributeName,String meetingName);
}
