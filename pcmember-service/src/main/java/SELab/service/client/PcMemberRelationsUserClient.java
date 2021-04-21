package SELab.service.client;

import SELab.domain.User;
import SELab.exception.InternalServerError;
import SELab.service.clientAPI.PcMemberRelationsUserClientAPI;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PcMemberRelationsUserClient {
    @Autowired
    PcMemberRelationsUserClientAPI pcMemberRelationsUserClientAPI;

    public User findByUsername(String username) {
        return getUserFromResponseEntity(pcMemberRelationsUserClientAPI.findByUsername(username));
    }

    public User findById(long id) {
        return getUserFromResponseEntity(pcMemberRelationsUserClientAPI.findById(id));
    }

    private User getUserFromResponseEntity(ResponseEntity<?> userResponseEntity) {
        if (userResponseEntity.getStatusCode() != HttpStatus.OK) {
            throw new InternalServerError(userResponseEntity.getBody().toString());
        }
        HashMap responseBody = (HashMap) ((HashMap) userResponseEntity.getBody()).get("responseBody");
        if (responseBody.get("UserInformation") != null) {
            return JSON.parseObject(JSON.toJSONString(responseBody.get("UserInformation")), User.class);
        } else {
            throw new InternalServerError(userResponseEntity.getBody().toString());
        }
    }
}
