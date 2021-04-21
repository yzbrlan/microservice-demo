package SELab.service;

import SELab.request.util.LoginRequest;
import SELab.request.util.RegisterRequest;
import SELab.utility.response.ResponseWrapper;

public interface UserService {
    ResponseWrapper<?> register(RegisterRequest request);
    ResponseWrapper<?> login(LoginRequest request);
    ResponseWrapper<?> getUserinfo(String username);
    ResponseWrapper<?> searchUsersbyFullname(String fullname);
    byte[] getPdfContent(String pdfUrl);

    ResponseWrapper<?> findByUsername(String username);
    ResponseWrapper<?> findById(long id);

    ResponseWrapper<?> findByFullnameAndEmail(String fullname, String email);
}
