package SELab.service.client;

import SELab.domain.Meeting;
import SELab.domain.PCMemberRelation;
import SELab.exception.MeetingOfNoExistenceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
public class PcMemberRelationClientImpl implements PcMemberRelationClient {
    @Autowired
    private RestTemplate restTemplate;

    private String getUrl(String uri) {
        return String.format("http://pcmember-service/pcMemberRelation/%s", uri);
    }

    public List<PCMemberRelation> findByMeetingIdAndStatus(long meetingId, String status){
        String url = getUrl("findByMeetingIdAndStatus?meetingId=" + meetingId + "&status=" + status);
        return queryPcMemberRelationList(url);
    }

    private List<PCMemberRelation> queryPcMemberRelationList(String url) {
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new MeetingOfNoExistenceException(responseEntity.getBody().toString());
        }
        LinkedHashMap hashMap = (LinkedHashMap) responseEntity.getBody().get("responseBody");
        if (hashMap.get("pcMemberRelationList") != null) {
            return new ArrayList<PCMemberRelation>((Collection<? extends PCMemberRelation>) hashMap.get("pcMemberRelationList"));
        } else if (hashMap.get("pcMemberRelations") != null) {
            ArrayList<PCMemberRelation> result=new ArrayList<>();
            JSONArray pcMemberRelations = JSON.parseArray(JSON.toJSONString(hashMap.get("pcMemberRelations")));
            for (Object object : pcMemberRelations) {
                PCMemberRelation pcMemberRelation = JSON.parseObject(object.toString(), PCMemberRelation.class);
                result.add(pcMemberRelation);
            }
            return result;
//            return new ArrayList<PCMemberRelation>((Collection<? extends PCMemberRelation>) hashMap.get("pcMemberRelations"));
        } else {
            return new ArrayList<>();
        }
    }
}
