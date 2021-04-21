package SELab.service;

import SELab.domain.Meeting;
import SELab.exception.MeetingOfNoExistenceException;
import SELab.exception.MeetingStatusUnAvailableToReviewException;
import SELab.exception.MeetingUnavaliableToOperateException;
import SELab.repository.MeetingRepository;
import SELab.request.admin.ApplicationRatifyRequest;
import SELab.request.meeting.BeginSubmissionRequest;
import SELab.request.meeting.FinalPublishRequest;
import SELab.request.meeting.ResultPublishRequest;
import SELab.utility.contract.MeetingStatus;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xywu
 * @date 2020/11/02
 */
@Service
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    MeetingRepository meetingRepository;

    @Transactional
    public ResponseWrapper<?> chairMeeting(String username) {
        List<Meeting> meetingList = meetingRepository.findByChairName(username);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for (Meeting meeting : meetingList) {
            HashMap<String, Object> meetingInfo = ResponseGenerator.generate(meeting,
                    new String[]{"id","meetingName", "acronym", "conferenceDate", "topic"}, null);
            response.add(meetingInfo);
        }
        body.put("meetings", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Transactional
    public ResponseWrapper<?> availableMeeting(String username) {
        List<Meeting> allMeeting = meetingRepository.findByStatusAndChairNameNot(MeetingStatus.submissionAvaliable, username);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for (Meeting meeting : allMeeting) {
            response.add(ResponseGenerator.generate(meeting,
                    new String[]{"meetingName", "acronym", "submissionDeadlineDate", "topic"}, null));
        }
        body.put("meetings", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    public ResponseWrapper<?> finalPublish(FinalPublishRequest request) {
        Meeting meeting = meetingRepository.findByMeetingName(request.getMeetingName());
        if (meeting.getStatus().equals(MeetingStatus.reviewFinish)) {
            meeting.setStatus(MeetingStatus.reviewPublish);
            meetingRepository.save(meeting);
            return new ResponseWrapper<>(200, ResponseGenerator.success, null);
        } else {
            return new ResponseWrapper<>(200, "failed: unable to do final publish for incorrect meeting status", null);
        }
    }

    @Transactional
    public ResponseWrapper<?> getMeetingInfo(String meetingName) {
        Meeting meeting = meetingRepository.findByMeetingName(meetingName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }

        return ResponseGenerator.injectObjectFromObjectToResponse("meetingInfo", meeting, new String[]{"chairName", "meetingName", "acronym", "region", "city", "venue", "topic", "organizer", "webPage", "submissionDeadlineDate", "notificationOfAcceptanceDate", "conferenceDate", "status"}, null);
    }

    @Transactional
    public ResponseWrapper<?> beginSubmission(BeginSubmissionRequest request) {
        String meetingName = request.getMeetingName();
        Meeting meeting = meetingRepository.findByMeetingName(meetingName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }

        String meetingStatus = meeting.getStatus();
        if (!meetingStatus.equals(MeetingStatus.applyPassed)) {
            throw new MeetingUnavaliableToOperateException(meetingName);
        }
        meeting.setStatus(MeetingStatus.submissionAvaliable);
        meetingRepository.save(meeting);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    public ResponseWrapper<?> reviewPublish(ResultPublishRequest request) {
        Meeting meeting = meetingRepository.findByMeetingName(request.getMeetingName());
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(request.getMeetingName());
        }//会议是否存在
        if (!meeting.getStatus().equals(MeetingStatus.reviewCompleted)) {
            throw new MeetingStatusUnAvailableToReviewException();
        }
        meeting.setStatus(MeetingStatus.resultPublished);
        meetingRepository.save(meeting);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    @Override
    public ResponseWrapper<?> getQueueingApplication() {
        List<Meeting> meeting = meetingRepository.findByStatus(MeetingStatus.unprocessed);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(MeetingStatus.unprocessed);
        }
        return ResponseGenerator.injectObjectFromListToResponse("queueingApplication", meeting,
                new String[]{"id", "chairName", "meetingName", "acronym", "region", "city", "venue", "topic",
                        "organizer", "webPage", "submissionDeadlineDate", "notificationOfAcceptanceDate",
                        "conferenceDate", "status"}, null);
    }

    @Override
    public ResponseWrapper<?> getAlreadyApplication() {
        List<Meeting> meeting = meetingRepository.findByStatusNot(MeetingStatus.unprocessed);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(MeetingStatus.unprocessed);
        }
        return ResponseGenerator.injectObjectFromListToResponse("alreadyApplication", meeting,
                new String[]{"id", "chairName", "meetingName", "acronym", "region", "city", "venue", "topic",
                        "organizer", "webPage", "submissionDeadlineDate", "notificationOfAcceptanceDate",
                        "conferenceDate", "status"}, null);
    }

    @Override
    public ResponseWrapper<?> applicationRatify(ApplicationRatifyRequest request) {
        String meetingName = request.getMeetingName();
        Meeting meeting = meetingRepository.findByMeetingName(meetingName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }
        if (!meeting.getStatus().equals(MeetingStatus.unprocessed)) {
            throw new MeetingUnavaliableToOperateException(meetingName);
        }
        meeting.setStatus(request.getApprovalStatus());
        meetingRepository.save(meeting);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    @Override
    public ResponseWrapper<?> findById(long id) {
        Meeting meeting = meetingRepository.findById(id);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(id + "");
        }
        return ResponseGenerator.injectObjectFromObjectToResponse("meeting", meeting,
                new String[]{"id", "chairName", "meetingName", "acronym", "region", "city", "venue", "topic",
                        "organizer", "webPage", "submissionDeadlineDate", "notificationOfAcceptanceDate",
                        "conferenceDate", "status"}, null);
    }

    @Override
    public ResponseWrapper<?> findByMeetingName(String meetingName) {
        Meeting meeting = meetingRepository.findByMeetingName(meetingName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }
        return ResponseGenerator.injectObjectFromObjectToResponse("meeting", meeting, new String[]{"id", "chairName", "meetingName", "acronym", "region", "city", "venue", "topic", "organizer", "webPage", "submissionDeadlineDate", "notificationOfAcceptanceDate", "conferenceDate", "status"}, null);
    }

    @Override
    public ResponseWrapper<?> findByStatus(String status) {
        List<Meeting> meeting = meetingRepository.findByStatus(status);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(status);
        }
        return ResponseGenerator.injectObjectFromListToResponse("meetingList", meeting,
                new String[]{"id", "chairName", "meetingName", "acronym", "region", "city", "venue", "topic",
                        "organizer", "webPage", "submissionDeadlineDate", "notificationOfAcceptanceDate",
                        "conferenceDate", "status"}, null);
    }

    @Override
    public ResponseWrapper<?> findByStatusNot(String status) {
        List<Meeting> meeting = meetingRepository.findByStatusNot(status);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(status);
        }
        return ResponseGenerator.injectObjectFromListToResponse("meetingList", meeting,
                new String[]{"id", "chairName", "meetingName", "acronym", "region", "city", "venue", "topic",
                        "organizer", "webPage", "submissionDeadlineDate", "notificationOfAcceptanceDate",
                        "conferenceDate", "status"}, null);
    }

    @Override
    public ResponseWrapper<?> findByChairName(String chairName) {
        List<Meeting> meeting = meetingRepository.findByChairName(chairName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(chairName);
        }
        return ResponseGenerator.injectObjectFromListToResponse("meetingList", meeting,
                new String[]{"id", "chairName", "meetingName", "acronym", "region", "city", "venue", "topic",
                        "organizer", "webPage", "submissionDeadlineDate", "notificationOfAcceptanceDate",
                        "conferenceDate", "status"}, null);
    }

    @Override
    public ResponseWrapper<?> findByStatusAndChairNameNot(String status, String chairName) {
        List<Meeting> meeting = meetingRepository.findByStatusAndChairNameNot(status, chairName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(chairName);
        }
        return ResponseGenerator.injectObjectFromListToResponse("meetingList", meeting,
                new String[]{"id", "chairName", "meetingName", "acronym", "region", "city", "venue", "topic",
                        "organizer", "webPage", "submissionDeadlineDate", "notificationOfAcceptanceDate",
                        "conferenceDate", "status"}, null);
    }

    @Transactional
    public ResponseWrapper<?> save(Meeting meeting) {
        meetingRepository.save(meeting);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }
}
