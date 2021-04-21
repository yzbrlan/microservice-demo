package SELab.service;

import SELab.service.client.ArticleClient;
import SELab.service.client.PcMemberRelationClient;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class MessageServiceImpl implements MessageService {

    Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    ArticleClient articleClient;

    @Autowired
    PcMemberRelationClient pcMemberRelationClient;

    public ResponseWrapper<?> findArticleMessagesByChairName(String chairName){
        return new ResponseWrapper<>(200, ResponseGenerator.success, articleClient.getArticleMessagesByChairName(chairName));
    }

    public ResponseWrapper<?> findArticleResultMessagesByAuthorName(String authorName){
        return new ResponseWrapper<>(200, ResponseGenerator.success, articleClient.getArticleResultMessagesByAuthorName(authorName));
    }

    public ResponseWrapper<?> findInvitationResultMessagesByChairName(String chairName){
        return new ResponseWrapper<>(200, ResponseGenerator.success, pcMemberRelationClient.getInvitationResultMessagesByChairName(chairName));
    }

    public ResponseWrapper<?> findReviewingMeetingMessagesByPCMemberName(String pcMemberName){
        return new ResponseWrapper<>(200, ResponseGenerator.success, pcMemberRelationClient.getArticleResultMessagesByAuthorName(pcMemberName) );
    }

}
