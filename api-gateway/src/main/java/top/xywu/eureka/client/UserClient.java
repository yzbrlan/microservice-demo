package top.xywu.eureka.client;

import top.xywu.eureka.domain.User;

/**
 * @author xywu
 * @date 2020/11/06
 */
public interface UserClient {
    User findByUsername(String username);
    User findById(long id);
}
