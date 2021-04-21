package SELab.service.client;

import SELab.domain.Article;
import SELab.domain.Author;
import SELab.exception.InternalServerError;
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

    public List<String> getArticleMessagesByChairName(String chairName){
        String url = getUrl("getArticleMessagesByChairName?chairName=" + chairName);
        return queryMessages(url);
    }

    public List<String> getArticleResultMessagesByAuthorName(String authorName){
        String url = getUrl("getArticleResultMessagesByAuthorName?authorName=" +  authorName);
        return queryMessages(url);
    }

    private List<String> queryMessages(String url) {
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new InternalServerError(responseEntity.getBody().toString());
        }
        LinkedHashMap hashMap = (LinkedHashMap) responseEntity.getBody().get("responseBody");
        if (hashMap.get("messages") != null) {
            ArrayList<String> result = new ArrayList<>();
            JSONArray messages = JSON.parseArray(JSON.toJSONString(hashMap.get("messages")));
            for (Object object : messages) {
                result.add(object.toString());
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }

}
