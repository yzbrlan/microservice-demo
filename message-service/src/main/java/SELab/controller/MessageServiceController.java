package SELab.controller;

import SELab.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageServiceController {

    @Autowired
    MessageService messageService;

    Logger logger = LoggerFactory.getLogger(MessageServiceController.class);


    @GetMapping("/getArticleMessagesByChairName")
    public ResponseEntity<?> getArticleMessagesByChairName(String chairName) {
        return ResponseEntity.ok(messageService.findArticleMessagesByChairName(chairName));
    }

    @GetMapping("/getArticleResultMessagesByAuthorName")
    public ResponseEntity<?> getArticleResultMessagesByAuthorName(String authorName) {
        return ResponseEntity.ok(messageService.findArticleResultMessagesByAuthorName(authorName));
    }

    @GetMapping("/getReviewingMeetingMessagesByPCMemberName")
    public ResponseEntity<?> getReviewingMeetingMessagesByPCMemberName(String pcMemberName){
        logger.debug("PCMember Name: " + pcMemberName);
        return ResponseEntity.ok(messageService.findReviewingMeetingMessagesByPCMemberName(pcMemberName));
    }

    @GetMapping("/getInvitationResultMessagesByChairName")
    public ResponseEntity<?> getInvitationResultMessagesByChairName(String chairName) {
        logger.debug("Chair Name: " + chairName);
        return ResponseEntity.ok(messageService.findInvitationResultMessagesByChairName(chairName));
    }

}
