package SELab.controller;

import SELab.service.PostService;
import SELab.request.meeting.ReviewPostRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;


/**
 * @author zty
 * @date 2020/11/6
 */
@RestController
public class PostServiceController {

    @Autowired
    PostService service;

    Logger logger = LoggerFactory.getLogger(PostServiceController.class);

    @PostMapping("/meeting/reviewPost")
    public ResponseEntity<?> reviewPost(
            @RequestParam("posterName") String posterName,
            @RequestParam("articleId") String articleId,
            @RequestParam("targetId") String targetId,
            @RequestParam("content") String content,
            @RequestParam("status") String status
    ) {
        ReviewPostRequest reviewPostRequest = new ReviewPostRequest(posterName, articleId, targetId, content, status);
        return ResponseEntity.ok(service.reviewPost(reviewPostRequest));
    }

    @GetMapping("/meeting/postList")
    public ResponseEntity<?> getPostList(long articleId, String postStatus) {
        return ResponseEntity.ok(service.getPostList(articleId, postStatus));
    }
}
