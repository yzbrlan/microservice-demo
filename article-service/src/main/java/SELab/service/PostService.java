package SELab.service;

import SELab.utility.response.ResponseWrapper;
import SELab.request.meeting.ReviewPostRequest;

/**
 * @author zty
 * @data 2020/11/6
 */

public interface PostService {
    ResponseWrapper<?> reviewPost(ReviewPostRequest request);
    ResponseWrapper<?> getPostList(long articleId, String postStatus);
}
