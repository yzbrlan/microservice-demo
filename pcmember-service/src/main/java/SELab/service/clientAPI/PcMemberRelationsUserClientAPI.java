package SELab.service.clientAPI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service")
public interface PcMemberRelationsUserClientAPI {
    @RequestMapping(value = "/user/findByUsername", method = RequestMethod.GET)
    ResponseEntity<?> findByUsername(@RequestParam("username") String username);

    @RequestMapping(value = "/user/findById", method = RequestMethod.GET)
    ResponseEntity<?> findById(@RequestParam("id") long id);
}
