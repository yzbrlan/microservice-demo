package SELab.service.client;

import SELab.domain.Meeting;
import SELab.service.clientAPI.PcMemberRelationsMeetingClientAPI;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Service
public class PcMemberRelationsMeetingClient {
    @Autowired
    PcMemberRelationsMeetingClientAPI pcMemberRelationsMeetingClientAPI;

    public Meeting findByMeetingName(String meetingName) {
        return getMeetingFromResponseEntity(pcMemberRelationsMeetingClientAPI.findByMeetingName(meetingName));
    }

    public Meeting findById(long meetingId) {
        return getMeetingFromResponseEntity(pcMemberRelationsMeetingClientAPI.findById(meetingId));
    }

    public ArrayList<Meeting> chairMeeting(String username) {
        return getMeetingsFromResponseEntity(pcMemberRelationsMeetingClientAPI.chairMeeting(username));
    }

    public ArrayList<Meeting> findByStatus(String status) {
        return getMeetingsFromResponseEntity(pcMemberRelationsMeetingClientAPI.findByStatus(status));
    }

    public void save(Meeting meeting) {
        pcMemberRelationsMeetingClientAPI.save(meeting);
    }

    private Meeting getMeetingFromResponseEntity(ResponseEntity<?> meetingResponseEntity) {
        if (meetingResponseEntity.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        HashMap responseBody = (HashMap) ((HashMap) meetingResponseEntity.getBody()).get("responseBody");
        if (responseBody.get("meeting") != null) {
            return JSON.parseObject(JSON.toJSONString(responseBody.get("meeting")), Meeting.class);
        } else {
            return null;
        }
    }

    private ArrayList<Meeting> getMeetingsFromResponseEntity(ResponseEntity<?> meetingsResponseEntity) {
        ArrayList<Meeting> result = new ArrayList<>();
        if (meetingsResponseEntity.getStatusCode() != HttpStatus.OK) {
            return result;
        }
        HashMap responseBody = (HashMap) ((HashMap) meetingsResponseEntity.getBody()).get("responseBody");
        if (responseBody.get("meetings") != null) {
            JSONArray meetings = JSON.parseArray(JSON.toJSONString(responseBody.get("meetings")));
            for (Object object : meetings) {
                Meeting meeting = JSON.parseObject(object.toString(), Meeting.class);
                result.add(meeting);
            }
        }else if (responseBody.get("meetingList") != null){
            JSONArray meetings = JSON.parseArray(JSON.toJSONString(responseBody.get("meetingList")));
            for (Object object : meetings) {
                Meeting meeting = JSON.parseObject(object.toString(), Meeting.class);
                result.add(meeting);
            }
        }
        return result;
    }
}
