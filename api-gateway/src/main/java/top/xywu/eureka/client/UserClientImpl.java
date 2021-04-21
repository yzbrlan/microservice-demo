package top.xywu.eureka.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.xywu.eureka.domain.User;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xywu
 * @date 2020/11/06
 */
@Service
public class UserClientImpl implements UserClient {
    @Autowired
    private RestTemplate restTemplate;

    private String getUrl(String uri) {
        return String.format("http://user-service/user/%s", uri);
    }

    @Override
    public User findByUsername(String username) {
        String url = getUrl("findByUsername?username=" + username);
        return queryUser(url);
    }

    @Override
    public User findById(long id) {
        String url = getUrl("findById?id=" + id);
        return queryUser(url);
    }

    private User queryUser(String url) {
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException(responseEntity.getBody().toString());
        }
        LinkedHashMap hashMap = (LinkedHashMap) responseEntity.getBody().get("responseBody");
        if (hashMap.get("UserInformation") != null) {
            JSONObject json =  new JSONObject((Map)hashMap.get("UserInformation"));
            return JSON.toJavaObject(json,User.class);
        } else {
            return null;
        }
    }
}
