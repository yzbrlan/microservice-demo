package SELab.service;

import SELab.domain.Article;
import SELab.domain.Meeting;
import SELab.domain.Rebuttal;
import SELab.repository.ArticleRepository;
import SELab.repository.RebuttalRepository;
import SELab.request.meeting.RebuttalRequest;
import SELab.service.client.MeetingClient;
import SELab.service.client.UserClient;
import SELab.utility.contract.ArticleStatus;
import SELab.utility.contract.MeetingStatus;
import SELab.utility.contract.RebuttalStatus;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RebuttalServiceImpl implements RebuttalService {

    @Autowired
    RebuttalRepository rebuttalRepository;

    @Autowired
    MeetingClient meetingClient;

    @Autowired
    UserClient userClient;

    @Autowired
    ArticleRepository articleRepository;
    Logger logger = LoggerFactory.getLogger(RebuttalServiceImpl.class);

    @Transactional
    public ResponseWrapper<?> rebuttal(RebuttalRequest request) {
        Article article = articleRepository.findById((long)Long.valueOf(request.getArticleId()));
        Meeting meeting = meetingClient.findByMeetingName(article.getMeetingname());
        if(!meeting.getStatus().equals(MeetingStatus.reviewConfirmed)){
            return new ResponseWrapper<>(200, "failed : current status unable to rebuttal for meeting status isn't being reviewConfirmed", null);
        }
        if(!article.getStatus().equals(ArticleStatus.rejected) && request.getStatus().equals(RebuttalStatus.rebuttal)){//没有被拒绝
            return new ResponseWrapper<>(200, "failed : current status unable to rebuttal for accepted article", null);
        }
        Rebuttal rebuttal = new Rebuttal(Long.valueOf(request.getArticleId()),request.getContent(),request.getStatus());
        rebuttalRepository.save(rebuttal);
        if(rebuttalRepository.findByIdNot(-1).size()==articleRepository.findByIdNot(-1).size()){
            meeting.setStatus(MeetingStatus.rebuttalFnish);
            meetingClient.save(meeting);
        }
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    @Transactional
    public ResponseWrapper<?> getRebuttalInfo(String articleId) {
        List<Rebuttal> rebuttals = rebuttalRepository.findByArticleId(Long.valueOf(articleId));
        if(rebuttals.isEmpty()){
            return new ResponseWrapper<>(200, "failed : no rebuttal for this article exist", null);
        }
        else{
            Rebuttal rebuttal = rebuttals.get(0);
            return ResponseGenerator.injectObjectFromObjectToResponse("rebuttal",rebuttal,new String[]{"content"},null);
        }
    }


}
