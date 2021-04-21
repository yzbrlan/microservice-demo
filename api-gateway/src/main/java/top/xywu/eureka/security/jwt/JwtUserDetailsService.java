package top.xywu.eureka.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import top.xywu.eureka.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.xywu.eureka.client.UserClient;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserClient userClient;

    public JwtUserDetailsService(){};
    @Autowired
    public JwtUserDetailsService(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userClient.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User: '" + username + "' not found.");
        } else {
            return new JwtUser(user.getId(), user.getUsername(), user.getPassword(), user.getAuthorities(), user.isEnabled());
        }
    }
}