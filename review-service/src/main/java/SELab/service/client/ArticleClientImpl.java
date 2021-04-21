package SELab.service.client;

import SELab.domain.Article;
import SELab.domain.Author;
import SELab.exception.user.ArticleNotFoundException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author xywu
 * @date 2020/11/03
 */
@Service
public class ArticleClientImpl implements ArticleClient {
    @Autowired
    private RestTemplate restTemplate;

    private String getUrl(String uri) {
        return String.format("http://article-service/article/%s", uri);
    }

    @Override
    public void save(Article article) {
        String url = getUrl("save");

        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(url, article, HashMap.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new ArticleNotFoundException(responseEntity.getBody().toString());
        }
    }

    @Override
    public Article findById(long id) {
        String url = getUrl("findById?id=" + id);
        return queryArticle(url);
    }

    @Override
    public List<Article> findByIdNot(long id) {
        return null;
    }

    @Override
    public List<Article> findByContributorName(String contributorName) {
        return null;
    }

    @Override
    public List<Article> findByMeetingName(String meetingName) {
        return null;
    }

    @Override
    public List<Article> findByMeetingNameAndStatus(String meetingName, String status) {
        String url = getUrl("findByMeetingNameAndStatus?meetingName=" + meetingName + "&status=" + status);
        return queryArticleList(url);
    }

    @Override
    public List<Article> findByContributorNameAndMeetingName(String contributeName, String meetingName) {
        return null;
    }

    private Article queryArticle(String url) {
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new ArticleNotFoundException(responseEntity.getBody().toString());
        }
        LinkedHashMap hashMap = (LinkedHashMap) responseEntity.getBody().get("responseBody");
        if (hashMap.get("articleDetail") != null) {
            JSONObject json = new JSONObject((Map) hashMap.get("articleDetail"));
            JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(json.get("authors")));
            Set<Pair<Author, Integer>> authors = new HashSet<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Author author = JSON.toJavaObject(jsonObject, Author.class);
                authors.add(Pair.of(author, i));
            }
            json.remove("authors");
            Article article = JSON.parseObject(json.toString(), Article.class);
            article.setAuthors(authors);
            return article;
        } else {
            return null;
        }
    }

    private List<Article> queryArticleList(String url) {
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new ArticleNotFoundException(responseEntity.getBody().toString());
        }
        LinkedHashMap hashMap = (LinkedHashMap) responseEntity.getBody().get("responseBody");
        if (hashMap.get("articles") != null) {
            ArrayList<Article> result = new ArrayList<>();
            JSONArray articles = JSON.parseArray(JSON.toJSONString(hashMap.get("articles")));
            for (Object object : articles) {
                JSONArray jsonArray = JSON.parseArray((((JSONObject) object).get("authors")).toString());
                Set<Pair<Author, Integer>> authors = new HashSet<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Author author = JSON.toJavaObject((JSONObject) jsonObject.get("key"), Author.class);
                    authors.add(Pair.of(author, i));
                }
                ((JSONObject) object).remove("authors");
                Article article = JSON.parseObject(object.toString(), Article.class);
                article.setAuthors(authors);
                result.add(article);
            }
            return result;
//            return new ArrayList<Article>((Collection<? extends Article>) hashMap.get("articles"));
        } else {
            return new ArrayList<>();
        }
    }

}
