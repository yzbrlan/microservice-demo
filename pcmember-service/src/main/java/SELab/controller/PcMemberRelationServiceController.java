package SELab.controller;

import SELab.request.meeting.MeetingApplicationRequest;
import SELab.request.meeting.PCMemberInvitationRequest;
import SELab.request.user.InvitationRepoRequest;
import SELab.service.PcMemberRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PcMemberRelationServiceController {

    @Autowired
    PcMemberRelationService pcMemberRelationService;

    Logger logger = LoggerFactory.getLogger(PcMemberRelationServiceController.class);

    @GetMapping("/user/pcMemberMeeting")
    public ResponseEntity<?> pcMemberMeeting(String username) {
        logger.debug("Get pcMemberMeeting info : " + username);
        return ResponseEntity.ok(pcMemberRelationService.pcMemberMeeting(username));
    }

    @PostMapping("/user/invitationRepo")
    public ResponseEntity<?> invitationRepo(@RequestBody InvitationRepoRequest request) {
        logger.debug("Post invitationRepo info : " + request.toString());
        return ResponseEntity.ok(pcMemberRelationService.invitationRepo(request));
    }

    @GetMapping("/user/undealedNotificationsNum")
    public ResponseEntity<?> undealedNotificationsNum(String username) {
        logger.debug("Get undealedNotificationsNum info : " + username);
        return ResponseEntity.ok(pcMemberRelationService.undealedNotificationsNum(username));
    }

    @GetMapping("/user/undealedNotifications")
    public ResponseEntity<?> undealedNotifications(String username) {
        logger.debug("Get undealedNotifications info : " + username);
        return ResponseEntity.ok(pcMemberRelationService.undealedNotifications(username));
    }

    @GetMapping("/user/alreadyDealedNotifications")
    public ResponseEntity<?> alreadyDealedNotifications(String username) {
        logger.debug("Get alreadyDealedNotifications info : " + username);
        return ResponseEntity.ok(pcMemberRelationService.alreadyDealedNotifications(username));
    }

    @PostMapping("/meeting/application")
    public ResponseEntity<?> meetingApplication(@RequestBody MeetingApplicationRequest request) {
        logger.debug("Meeting application: " + request.toString());
        return ResponseEntity.ok(pcMemberRelationService.meetingApplication(request));
    }

    @PostMapping("/meeting/pcmInvitation")
    public ResponseEntity<?> pcmInvitation(@RequestBody PCMemberInvitationRequest request) {
        logger.debug("PCMember Invitation: " + request.toString());
        return ResponseEntity.ok(pcMemberRelationService.pcmInvitation(request));
    }

    @GetMapping("/meeting/invitationStatus")
    public ResponseEntity<?> getInvitationStatus(String meetingName) {
        logger.debug("Invitation Status: " + meetingName);
        return ResponseEntity.ok(pcMemberRelationService.getInvitationStatus(meetingName));
    }

    @GetMapping("/pcMemberRelation/findByMeetingIdAndStatus")
    public ResponseEntity<?> getPcMemberRelationsByMeetingIdAndStatus(long meetingId, String status) {
        logger.debug("Meeting Id: " + meetingId + ", Status: " + status);
        return ResponseEntity.ok(pcMemberRelationService.findByMeetingIdAndStatus(meetingId, status));
    }

    @GetMapping("/getInvitationResultMessagesByChairName")
    public ResponseEntity<?> getInvitationResultMessagesByChairName(String chairName) {
        logger.debug("Chair Name: " + chairName);
        return ResponseEntity.ok(pcMemberRelationService.findInvitationResultMessagesByChairName(chairName));
    }
    @GetMapping("/getReviewingMeetingMessagesByPCMemberName")
    public ResponseEntity<?> getReviewingMeetingMessagesByPCMemberName(String pcMemberName){
        logger.debug("PCMember Name: " + pcMemberName);
        return ResponseEntity.ok(pcMemberRelationService.findReviewingMeetingMessagesByPCMemberName(pcMemberName));
    }
}
