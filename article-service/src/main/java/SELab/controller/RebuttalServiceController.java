package SELab.controller;

import SELab.request.meeting.RebuttalRequest;
import SELab.service.RebuttalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RebuttalServiceController {

    @Autowired
    RebuttalService service;

    Logger logger = LoggerFactory.getLogger(RebuttalServiceController.class);

    @PostMapping("/meeting/rebuttal")
    public ResponseEntity<?> rebuttal(@RequestBody RebuttalRequest request) {
        logger.debug("Rebuttal: " + request.toString());
        return ResponseEntity.ok(service.rebuttal(request));
    }

    @GetMapping("/meeting/rebuttalInfo")
    public ResponseEntity<?> getRebuttalInfo(String articleId) {
        logger.debug("Get Rebuttal Info for article: ID " + articleId);
        return ResponseEntity.ok(service.getRebuttalInfo(articleId));
    }

}
