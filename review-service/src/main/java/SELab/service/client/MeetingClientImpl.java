package SELab.service.client;

import SELab.domain.Meeting;
import SELab.exception.MeetingOfNoExistenceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
public class MeetingClientImpl implements MeetingClient {
    @Autowired
    private RestTemplate restTemplate;

    private String getUrl(String uri) {
        return String.format("http://meeting-service/meeting/%s", uri);
    }

    @Override
    public Meeting findByMeetingName(String meetingName) {
        String url = getUrl("findByMeetingName?meetingName=" + meetingName);
        return queryMeeting(url);
    }

    @Override
    public Meeting findById(long id) {
        String url = getUrl("findById?id=" + id);
        return queryMeeting(url);
    }

    @Override
    public List<Meeting> findByStatus(String status) {
        String url = getUrl("findByStatus?status" + status);
        return queryMeetingList(url);
    }

    @Override
    public List<Meeting> findByStatusNot(String status) {
        String url = getUrl("findByStatusNot?status=" + status);
        return queryMeetingList(url);
    }

    @Override
    public List<Meeting> findByChairName(String chairName) {
        String url = getUrl("findByChairName?chairName=" + chairName);
        return queryMeetingList(url);
    }

    @Override
    public List<Meeting> findByStatusAndChairNameNot(String status, String chairName) {
        String url = getUrl(String.format("findByStatusAndChairNameNot?status=%s&chairName=%s", status, chairName));
        return queryMeetingList(url);
    }

    @Override
    public void save(Meeting meeting) {
        String url = getUrl("save");

        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(url, meeting, HashMap.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new MeetingOfNoExistenceException(responseEntity.getBody().toString());
        }
    }

    private Meeting queryMeeting(String url) {
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new MeetingOfNoExistenceException(responseEntity.getBody().toString());
        }
        LinkedHashMap hashMap = (LinkedHashMap) responseEntity.getBody().get("responseBody");
        if (hashMap.get("meetingInfo") != null) {
            JSONObject json = new JSONObject((Map) hashMap.get("meetingInfo"));
            return JSON.toJavaObject(json, Meeting.class);
        } else if (hashMap.get("meeting") != null) {
            JSONObject json = new JSONObject((Map) hashMap.get("meeting"));
            return JSON.toJavaObject(json, Meeting.class);
        } else {
            return null;
        }
    }

    private List<Meeting> queryMeetingList(String url) {
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new MeetingOfNoExistenceException(responseEntity.getBody().toString());
        }
        LinkedHashMap hashMap = (LinkedHashMap) responseEntity.getBody().get("responseBody");
        if (hashMap.get("meetingList") != null) {
            return new ArrayList<Meeting>((Collection<? extends Meeting>) hashMap.get("meetingList"));
        } else {
            return new ArrayList<>();
        }
    }
}
