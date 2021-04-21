package SELab.service;

import SELab.request.meeting.BeginReviewRequest;
import SELab.request.meeting.ReviewConfirmRequest;
import SELab.request.meeting.ReviewRequest;
import SELab.request.meeting.UpdateReviewRequest;
import SELab.utility.response.ResponseWrapper;

public interface ReviewRelationService {

    ResponseWrapper<?> getAllReviews(String articleId);

    ResponseWrapper<?> updateReview(UpdateReviewRequest request);

    ResponseWrapper<?> reviewConfirm(ReviewConfirmRequest request);

    ResponseWrapper<?> getInfoOfReview(String pcMemberName, String meetingName);

    ResponseWrapper<?> getInfoOfArticleToReview(String pcMemberName, String articleId);

    ResponseWrapper<?> review(ReviewRequest request);

    ResponseWrapper<?> getAlreadyReviewedInfo(String pcMemberName, String articleId);

    ResponseWrapper<?> beginReview(BeginReviewRequest request);

}
