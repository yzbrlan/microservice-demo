package SELab.controller;

import SELab.domain.Meeting;
import SELab.request.admin.ApplicationRatifyRequest;
import SELab.request.meeting.BeginSubmissionRequest;
import SELab.request.meeting.FinalPublishRequest;
import SELab.request.meeting.ResultPublishRequest;
import SELab.service.MeetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeetingServiceController {
    Logger logger = LoggerFactory.getLogger(MeetingServiceController.class);

    @Autowired
    private MeetingService service;

    @GetMapping("/user/chairMeeting")
    public ResponseEntity<?> chairMeeting(String username) {
        logger.debug("Get chair meeting info: " + username);
        return ResponseEntity.ok(service.chairMeeting(username));
    }

    @GetMapping("/user/availableMeeting")
    public ResponseEntity<?> availableMeeting(String username) {
        logger.debug("Get available meeting info : " + username);
        return ResponseEntity.ok(service.availableMeeting(username));
    }

    @PostMapping("/meeting/finalPublish")
    public ResponseEntity<?> finalPublish(@RequestBody FinalPublishRequest request) {
        logger.debug("Final Publish: " + request.toString());
        return ResponseEntity.ok(service.finalPublish(request));
    }

    @GetMapping("/meeting/meetingInfo")
    public ResponseEntity<?> getmeetingInfo(String meetingName) {
        logger.debug("Meeting Information: " + meetingName);
        return ResponseEntity.ok(service.getMeetingInfo(meetingName));
    }

    @PostMapping("/meeting/beginSubmission")
    public ResponseEntity<?> beginSubmission(@RequestBody BeginSubmissionRequest request) {
        logger.debug("Begin Submission: " + request.toString());
        return ResponseEntity.ok(service.beginSubmission(request));
    }

    @PostMapping("/meeting/publish")
    public ResponseEntity<?> reviewPublish(@RequestBody ResultPublishRequest request) {
        logger.debug("Review Request to Publish: " + request.toString());
        return ResponseEntity.ok(service.reviewPublish(request));
    }

    @GetMapping("/admin/queueingApplication")
    public ResponseEntity<?> getqueueingApplication() {
        logger.debug("Get queuing applications by admin");
        return ResponseEntity.ok(service.getQueueingApplication());
    }

    @GetMapping("/admin/alreadyApplication")
    public ResponseEntity<?> getalreadyApplication() {
        logger.debug("Get dealed applications by admin");
        return ResponseEntity.ok(service.getAlreadyApplication());
    }

    @PostMapping("/admin/ratify")
    public ResponseEntity<?> applicationRatify(@RequestBody ApplicationRatifyRequest request) {
        logger.debug("Ratification for Meeting named " + request.getMeetingName());
        return ResponseEntity.ok(service.applicationRatify(request));
    }

    @GetMapping("/meeting/findById")
    public ResponseEntity<?> findById(long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/meeting/findByMeetingName")
    public ResponseEntity<?> findByMeetingName(String meetingName) {
        return ResponseEntity.ok(service.findByMeetingName(meetingName));
    }

    @GetMapping("/meeting/findByStatus")
    public ResponseEntity<?> findByStatus(String status) {
        return ResponseEntity.ok(service.findByStatus(status));

    }

    @GetMapping("/meeting/findByStatusNot")
    public ResponseEntity<?> findByStatusNot(String status) {
        return ResponseEntity.ok(service.findByStatusNot(status));

    }

    @GetMapping("/meeting/findByChairName")
    public ResponseEntity<?> findByChairName(String chairName) {
        return ResponseEntity.ok(service.findByChairName(chairName));

    }

    @GetMapping("/meeting/findByStatusAndChairNameNot")
    public ResponseEntity<?> findByStatusAndChairNameNot(String status, String chairName) {
        return ResponseEntity.ok(service.findByStatusAndChairNameNot(status, chairName));

    }

    @PostMapping("/meeting/save")
    public ResponseEntity<?> save(@RequestBody Meeting meeting) {
        return ResponseEntity.ok(service.save(meeting));
    }
}
