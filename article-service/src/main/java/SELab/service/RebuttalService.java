package SELab.service;
import SELab.request.meeting.RebuttalRequest;
import SELab.utility.response.ResponseWrapper;

public interface RebuttalService {
    ResponseWrapper<?> rebuttal(RebuttalRequest request);
    ResponseWrapper<?> getRebuttalInfo(String articleId);
}
