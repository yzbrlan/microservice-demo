package SELab.service;

import SELab.exception.InternalServerError;
import SELab.repository.PostRepository;
import SELab.request.meeting.ReviewPostRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import SELab.service.client.UserClient;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import SELab.domain.PostMessage;
import SELab.domain.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zty
 * @data 2020/11/6
 */

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;
    Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    UserClient userClient;

    @Transactional
    public ResponseWrapper<?> reviewPost(ReviewPostRequest request) {
        User poster = userClient.findByUsername(request.getPosterName());
        if (poster == null) {
            throw new InternalServerError("new internal error" + request.getPosterName());
        }
        Timestamp sqlTimestamp = new Timestamp(System.currentTimeMillis());
        PostMessage postMessage = new PostMessage(poster.getId(), Long.parseLong(request.getArticleId()), Long.parseLong(request.getTargetId()), request.getContent(), request.getStatus(), sqlTimestamp.toString());
        logger.info(postMessage.toString());
        postRepository.save(postMessage);
        return new ResponseWrapper<>(200, ResponseGenerator.success, postMessage);
    }

    @Transactional
    public ResponseWrapper<?> getPostList(long articleId, String postStatus) {
        List<PostMessage> postMessageList = postRepository.findByArticleIdAndStatus(articleId, postStatus);
        List<HashMap> postList = new ArrayList();
        HashMap<String, Object> postMap = new HashMap<>();
        for (PostMessage postMessage : postMessageList) {
            long posterId = postMessage.getPosterId();
            String posterName = userClient.findById(posterId).getUsername();
            if (posterName == null) {
                throw new InternalServerError("cannot find poster's name");
            }
            long targetId = postMessage.getTargetId();
            String targetName = userClient.findById(targetId).getUsername();
            if (targetName == null) {
                throw new InternalServerError("cannot find target's name");
            }
            postMap.clear();
            postMap.put("postId", postMessage.getId());
            postMap.put("targetContent", targetName);
            postMap.put("postContent", postMessage.getContent());
            postMap.put("posterName", posterName);
            postMap.put("timeStamp", postMessage.getTimeStamp());
            postList.add(postMap);
        }
        HashMap<String, List<HashMap>> body = new HashMap<>();
        body.put("postlist", postList);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }
}
