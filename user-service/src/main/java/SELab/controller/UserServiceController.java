package SELab.controller;

import SELab.request.util.LoginRequest;
import SELab.request.util.RegisterRequest;
import SELab.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserServiceController {

    @Autowired
    UserService service;

    Logger logger = LoggerFactory.getLogger(UserServiceController.class);

    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        Map<String, String> response = new HashMap<>();
        String message = "Welcome to 2020 Software Engineering Lab2";
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.debug("RegistrationForm: " + request.toString());

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        logger.debug("LoginForm: " + request.toString());

        return ResponseEntity.ok(service.login(request));
    }

    @GetMapping("/user/userinfo")
    public ResponseEntity<?> getUserinfo(String username) {
        logger.debug("Get user info: " + username);
        return ResponseEntity.ok(service.getUserinfo(username));
    }


    @GetMapping("/util/users")
    public ResponseEntity<?> searchUsersbyFullname(String fullname) {
        logger.debug("Users with fullname " + fullname + " : ");
        return ResponseEntity.ok(service.searchUsersbyFullname(fullname));
    }

    @GetMapping("/utils/pdf")
    public byte[] getImage(String pdfUrl) {
        logger.debug("Get file for pdfUrl " + pdfUrl + " : ");
        return service.getPdfContent(pdfUrl);
    }

    @GetMapping("/user/findByUsername")
    public ResponseEntity<?> findByUsername(String username) {
        logger.debug("Find user by username " + username + " : ");
        return ResponseEntity.ok(service.findByUsername(username));
    }

    @GetMapping("/user/findByFullnameAndEmail")
    public ResponseEntity<?> findByFullnameAndEmail(String fullname, String email) {
        logger.debug("Find user by fullname " + fullname + " and email : " + email);
        return ResponseEntity.ok(service.findByFullnameAndEmail(fullname,email));
    }

    @GetMapping("/user/findById")
    public ResponseEntity<?> findById(long id) {
        logger.debug("Find user by id " + id + " : ");
        return ResponseEntity.ok(service.findById(id));
    }
}
