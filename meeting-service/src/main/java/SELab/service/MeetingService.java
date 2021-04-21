package SELab.service;

import SELab.domain.Meeting;
import SELab.request.admin.ApplicationRatifyRequest;
import SELab.request.meeting.BeginSubmissionRequest;
import SELab.request.meeting.FinalPublishRequest;
import SELab.request.meeting.ResultPublishRequest;
import SELab.utility.response.ResponseWrapper;

/**
 * @author xywu
 * @date 2020/11/02
 */
public interface MeetingService {
    ResponseWrapper<?> chairMeeting(String username);

    ResponseWrapper<?> availableMeeting(String username);

    ResponseWrapper<?> finalPublish(FinalPublishRequest request);

    ResponseWrapper<?> getMeetingInfo(String meetingName);

    ResponseWrapper<?> beginSubmission(BeginSubmissionRequest request);

    ResponseWrapper<?> reviewPublish(ResultPublishRequest request);

    ResponseWrapper<?> getQueueingApplication();

    ResponseWrapper<?> getAlreadyApplication();

    ResponseWrapper<?> applicationRatify(ApplicationRatifyRequest request);

    ResponseWrapper<?> findById(long id);

    ResponseWrapper<?> findByMeetingName(String meetingName);

    ResponseWrapper<?> findByStatus(String status);

    ResponseWrapper<?> findByStatusNot(String status);

    ResponseWrapper<?> findByChairName(String chairName);

    ResponseWrapper<?> findByStatusAndChairNameNot(String status, String chairName);

    ResponseWrapper<?> save(Meeting meeting);
}
