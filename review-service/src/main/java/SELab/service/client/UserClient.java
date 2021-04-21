package SELab.service.client;

import SELab.domain.User;

/**
 * @author xywu
 * @date 2020/11/03
 */
public interface UserClient {
    User findByUsername(String username);
    User findById(long id);
    User findByEmail(String email);
    User findByFullnameAndEmail(String fullname,String email);
}
