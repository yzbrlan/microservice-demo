package SELab.service.clientAPI;

import SELab.domain.Meeting;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "meeting-service")
public interface PcMemberRelationsMeetingClientAPI {

    @RequestMapping(value = "/meeting/findById", method = RequestMethod.GET)
    ResponseEntity<?> findById(@RequestParam("id") long id);

    @RequestMapping(value = "/meeting/findByMeetingName", method = RequestMethod.GET)
    ResponseEntity<?> findByMeetingName(@RequestParam("meetingName") String meetingName);

    @RequestMapping(value = "/meeting/save", method = RequestMethod.POST)
    ResponseEntity<?> save(@RequestBody Meeting meeting);

    @RequestMapping(value = "/user/chairMeeting", method = RequestMethod.GET)
    ResponseEntity<?> chairMeeting(@RequestParam("username") String username);

    @RequestMapping(value = "/meeting/findByStatus", method = RequestMethod.GET)
    ResponseEntity<?> findByStatus(@RequestParam("status") String status);
}
