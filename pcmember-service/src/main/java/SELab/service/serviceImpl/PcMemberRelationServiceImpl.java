package SELab.service.serviceImpl;

import SELab.domain.Meeting;
import SELab.domain.PCMemberRelation;
import SELab.domain.User;
import SELab.exception.*;
import SELab.repository.PCMemberRelationRepository;
import SELab.request.meeting.MeetingApplicationRequest;
import SELab.request.meeting.PCMemberInvitationRequest;
import SELab.request.user.InvitationRepoRequest;
import SELab.service.PcMemberRelationService;
import SELab.service.client.PcMemberRelationsMeetingClient;
import SELab.service.client.PcMemberRelationsUserClient;
import SELab.utility.contract.MeetingStatus;
import SELab.utility.contract.PCmemberRelationStatus;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PcMemberRelationServiceImpl implements PcMemberRelationService {

    @Autowired
    PCMemberRelationRepository pcMemberRelationRepository;

    @Autowired
    PcMemberRelationsMeetingClient pcMemberRelationsMeetingClient;

    @Autowired
    PcMemberRelationsUserClient pcMemberRelationsUserClient;
    Logger logger = LoggerFactory.getLogger(PcMemberRelationServiceImpl.class);


    @Override
    public ResponseWrapper<?> pcMemberMeeting(String username) {

        User user = pcMemberRelationsUserClient.findByUsername(username);
        Long userId = user.getId();
//        Long userId = userRepository.findByUsername(username).getId();
        List<PCMemberRelation> relationList = pcMemberRelationRepository.findByPcmemberIdAndStatus(userId, PCmemberRelationStatus.accepted);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for (PCMemberRelation relation : relationList) {
            Meeting meeting = pcMemberRelationsMeetingClient.findById(relation.getMeetingId());
//            Meeting meeting = meetingRepository.findById((long) relation.getMeetingId());
            HashMap<String, Object> meetingInfo = ResponseGenerator.generate(meeting,
                    new String[]{"meetingName", "acronym", "conferenceDate", "topic"}, null);
            response.add(meetingInfo);
        }
        body.put("meetings", response);

        logger.debug("Meeting list " + username + " role as pcMember has been fetched.");

        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Override
    public ResponseWrapper<?> invitationRepo(InvitationRepoRequest request) {
        User user = pcMemberRelationsUserClient.findByUsername(request.getUsername());
        Long userId = user.getId();
        Meeting meeting = pcMemberRelationsMeetingClient.findByMeetingName(request.getMeetingName());
        Long meetingId = meeting.getId();
//        Long userId = userRepository.findByUsername(request.getUsername()).getId();
//        Long meetingId = meetingRepository.findByMeetingName(request.getMeetingName()).getId();
        List<PCMemberRelation> relationList = pcMemberRelationRepository.findByPcmemberIdAndMeetingId(userId, meetingId);
        for (PCMemberRelation relation : relationList) {
            if (relation.getStatus().equals(PCmemberRelationStatus.undealed)) {
                relation.setStatus(request.getResponse());
                if (request.getResponse().equals(PCmemberRelationStatus.accepted)) {
                    relation.setTopic(request.getTopics());
                }
                pcMemberRelationRepository.save(relation);
                break;
            }
        }

        logger.debug("Invitation Repo by " + request.getUsername() + "to " + request.getMeetingName() + " have done.");

        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    @Override
    public ResponseWrapper<?> undealedNotificationsNum(String username) {
        User user = pcMemberRelationsUserClient.findByUsername(username);
        Long userId = user.getId();
//        Long userId = userRepository.findByUsername(username).getId();
        List<PCMemberRelation> relationList = pcMemberRelationRepository.findByPcmemberIdAndStatus(userId, PCmemberRelationStatus.undealed);
        HashMap<String, Object> body = new HashMap<>();
        body.put("undealedNotificationsNum", relationList.size());

        logger.debug("the num of undealed messages of " + username + "has been fetched.");

        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    public ResponseWrapper<?> undealedNotifications(String username) {
        Long userId = pcMemberRelationsUserClient.findByUsername(username).getId();
        List<PCMemberRelation> relationList = pcMemberRelationRepository.findByPcmemberIdAndStatus(userId, PCmemberRelationStatus.undealed);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for (PCMemberRelation relation : relationList) {
            Meeting meeting = pcMemberRelationsMeetingClient.findById((long) relation.getMeetingId());
            HashMap<String, Object> invitationInfo = ResponseGenerator.generate(meeting,
                    new String[]{"meetingName", "chairName", "topic"}, null);
            response.add(invitationInfo);
        }
        body.put("invitations", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Override
    public ResponseWrapper<?> alreadyDealedNotifications(String username) {
        User user = pcMemberRelationsUserClient.findByUsername(username);
        Long userId = user.getId();
//        Long userId = userRepository.findByUsername(username).getId();
        List<PCMemberRelation> relationList1 = pcMemberRelationRepository.findByPcmemberIdAndStatusNot(userId, PCmemberRelationStatus.undealed);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for (PCMemberRelation relation : relationList1) {
            HashMap<String, Object> invitaionInfo = ResponseGenerator.generate(relation,
                    new String[]{"status"}, null);
            Meeting meeting = pcMemberRelationsMeetingClient.findById(relation.getMeetingId());
            invitaionInfo.put("meetingName", meeting.getMeetingName());
//            invitaionInfo.put("meetingName", meetingRepository.findById((long) relation.getMeetingId()).getMeetingName());
            invitaionInfo.put("chairName", meeting.getChairName());
//            invitaionInfo.put("chairName", meetingRepository.findById((long) relation.getMeetingId()).getChairName());
            response.add(invitaionInfo);
        }
        body.put("alreadyDealedNotifications", response);

        logger.debug("alreadyDealed messages of " + username + "has been fetched.");

        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Override
    public ResponseWrapper<?> meetingApplication(MeetingApplicationRequest request) {
        User user = pcMemberRelationsUserClient.findByUsername(request.getChairName());
        if (user == null) {
            throw new UserNamedidntExistException(request.getChairName());
        }//chair是否是一个用户
//        if (userRepository.findByUsername(request.getChairName()) == null) {
//            throw new UserNamedidntExistException(request.getChairName());
//        }//chair是否是一个用户

        if (request.getTopic().isEmpty()) {
            throw new AtLeastOneTopicException();
        }

        Meeting meeting = pcMemberRelationsMeetingClient.findByMeetingName(request.getMeetingName());
        if (meeting != null) {
            throw new MeetingNameHasbeenregisteredException(request.getMeetingName());
        }//会议名称是否可用

        meeting = new Meeting(request.getChairName(), request.getMeetingName(), request.getAcronym(), request.getRegion(), request.getCity(), request.getVenue(), request.getTopic(), request.getOrganizer(), request.getWebPage(), request.getSubmissionDeadlineDate(), request.getNotificationOfAcceptanceDate(), request.getConferenceDate(), MeetingStatus.unprocessed);
        pcMemberRelationsMeetingClient.save(meeting);
        Meeting newMeeting = pcMemberRelationsMeetingClient.findByMeetingName(meeting.getMeetingName());
        User chair = pcMemberRelationsUserClient.findByUsername(request.getChairName());
        PCMemberRelation pcMemberRelationForChair = new PCMemberRelation(chair.getId(), newMeeting.getId(), PCmemberRelationStatus.accepted, request.getTopic());
        pcMemberRelationRepository.save(pcMemberRelationForChair);

        logger.info("Meeting named " + request.getMeetingName() + " has been added");

        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    @Override
    public ResponseWrapper<?> pcmInvitation(PCMemberInvitationRequest request) {
        String meetingName = request.getMeetingName();
        Meeting meeting = pcMemberRelationsMeetingClient.findByMeetingName(request.getMeetingName());
//        Meeting meeting = meetingRepository.findByMeetingName(meetingName);

        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }//会议是否存在

        if (meeting.getChairName().equals(request.getPcMemberName())) {
            throw new InvitationTargetIsForbiddenException(meetingName);
        }//邀请对象不能是chair本人

        String meetingStatus = meeting.getStatus();
        if (meetingStatus.equals(MeetingStatus.applyFailed) || meetingStatus.equals(MeetingStatus.unprocessed) || meetingStatus.equals(MeetingStatus.reviewing) || meetingStatus.equals(MeetingStatus.reviewCompleted)) {
            throw new MeetingStatusUnavailableForPCMemberInvitationException(meetingName);
        }//会议状态是否允许进行成员邀请
        User user = pcMemberRelationsUserClient.findByUsername(request.getPcMemberName());
//        User user = userRepository.findByUsername(request.getPcMemberName());
        if (user == null) {
            throw new UserNamedidntExistException(request.getPcMemberName());
        }//邀请对象是否存在邀请

        PCMemberRelation pcMemberRelation = new PCMemberRelation(user.getId(), meeting.getId(), PCmemberRelationStatus.undealed, null);

        pcMemberRelationRepository.save(pcMemberRelation);

        logger.info("Invitation of Meeting named " + request.getMeetingName() + " to " + "User named " + request.getPcMemberName() + " has been added");

        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    @Override
    public ResponseWrapper<?> getInvitationStatus(String meetingName) {
        Meeting meeting = pcMemberRelationsMeetingClient.findByMeetingName(meetingName);
//        Meeting meeting = meetingRepository.findByMeetingName(meetingName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }//会议是否存在
        List<PCMemberRelation> pcMemberRelations = pcMemberRelationRepository.findByMeetingId(meeting.getId());
        Set<HashMap<String, Object>> responseSet = new HashSet<>();
        for (PCMemberRelation x : pcMemberRelations) {
            User user = pcMemberRelationsUserClient.findById(x.getPcmemberId());
//            User user = userRepository.findById((long) x.getPcmemberId());
            HashMap<String, Object> map = ResponseGenerator.generate(user, new String[]{"username", "fullname", "email", "institution", "region"}, null);
            map.put("status", x.getStatus());
            responseSet.add(map);
        }
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        body.put("invitationStatus", responseSet);

        logger.debug("Invitations of Meeting named " + meetingName + " have been fetched");

        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Override
    public ResponseWrapper<?> findByMeetingIdAndStatus(long meetingId, String status) {
        List<PCMemberRelation> pcMemberRelations = pcMemberRelationRepository.findByMeetingIdAndStatus(meetingId, status);
        HashMap<String, List<PCMemberRelation>> body = new HashMap<>();
        body.put("pcMemberRelations", pcMemberRelations);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Override
    public ResponseWrapper<?> findInvitationResultMessagesByChairName(String chairName) {
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<Meeting> meetings = pcMemberRelationsMeetingClient.chairMeeting(chairName);
        for (Meeting meeting : meetings) {
            List<PCMemberRelation> pcMemberRelations = pcMemberRelationRepository.findByMeetingIdAndStatusNot(meeting.getId(), PCmemberRelationStatus.undealed);
            for (PCMemberRelation pcMemberRelation : pcMemberRelations) {
                User user = pcMemberRelationsUserClient.findById(pcMemberRelation.getPcmemberId());
                if (!chairName.equals(user.getUsername()))
                    messages.add("Your invitation of inviting " + user.getUsername() + " to be " + meeting.getMeetingName() + "'s pcmember has been " + pcMemberRelation.getStatus());
            }
        }
        HashMap<String, ArrayList<String>> body = new HashMap<>();
        body.put("messages", messages);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Override
    public ResponseWrapper<?> findReviewingMeetingMessagesByPCMemberName(String pcMemberName) {
        ArrayList<String> messages = new ArrayList<>();

        User user = pcMemberRelationsUserClient.findByUsername(pcMemberName);
        if (user == null) {
            throw new UserNamedidntExistException(pcMemberName);
        }
        ArrayList<Meeting> meetings = pcMemberRelationsMeetingClient.findByStatus(MeetingStatus.reviewing);
        for (Meeting meeting : meetings) {
            if (meeting.getChairName().equals(user.getUsername()))
                continue;
            List<PCMemberRelation> pcMemberRelation = pcMemberRelationRepository.findByPcmemberIdAndMeetingId(user.getId(), meeting.getId());
            if (pcMemberRelation.size() > 0) {
                messages.add("The " + meeting.getMeetingName() + " meeting that you are the one of pcmembers  is reviewing.");
            }
        }
        HashMap<String, ArrayList<String>> body = new HashMap<>();
        body.put("messages", messages);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }
}
