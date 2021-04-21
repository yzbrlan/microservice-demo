package SELab.service;

import SELab.request.meeting.MeetingApplicationRequest;
import SELab.request.meeting.PCMemberInvitationRequest;
import SELab.request.user.InvitationRepoRequest;
import SELab.utility.response.ResponseWrapper;

public interface PcMemberRelationService {
    ResponseWrapper<?> pcMemberMeeting(String username);

    ResponseWrapper<?> invitationRepo(InvitationRepoRequest request);

    ResponseWrapper<?> undealedNotificationsNum(String username);

    ResponseWrapper<?> undealedNotifications(String username);

    ResponseWrapper<?> alreadyDealedNotifications(String username);

    ResponseWrapper<?> meetingApplication(MeetingApplicationRequest request);

    ResponseWrapper<?> pcmInvitation(PCMemberInvitationRequest request);

    ResponseWrapper<?> getInvitationStatus(String meetingName);

    ResponseWrapper<?> findByMeetingIdAndStatus(long meetingId, String status);

    ResponseWrapper<?> findInvitationResultMessagesByChairName(String chairName);

    ResponseWrapper<?> findReviewingMeetingMessagesByPCMemberName(String pcMemberName);
}
