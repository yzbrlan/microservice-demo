package SELab.service.client;

import SELab.domain.Article;

import java.util.List;

/**
 * @author xywu
 * @date 2020/11/03
 */
public interface ArticleClient {

    List<String> getArticleMessagesByChairName(String chairName);

    List<String> getArticleResultMessagesByAuthorName(String authorName);
}
