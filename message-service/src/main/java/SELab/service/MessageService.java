package SELab.service;

import SELab.request.util.LoginRequest;
import SELab.request.util.RegisterRequest;
import SELab.utility.response.ResponseWrapper;

public interface MessageService {

    ResponseWrapper<?> findArticleResultMessagesByAuthorName(String authorName);

    ResponseWrapper<?> findArticleMessagesByChairName(String chairName);

    ResponseWrapper<?> findInvitationResultMessagesByChairName(String chairName);

    ResponseWrapper<?> findReviewingMeetingMessagesByPCMemberName(String pcMemberName);


}
