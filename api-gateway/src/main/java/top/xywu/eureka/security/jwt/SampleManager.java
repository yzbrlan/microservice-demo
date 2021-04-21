package top.xywu.eureka.security.jwt;

import top.xywu.eureka.client.UserClient;
import top.xywu.eureka.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class SampleManager implements AuthenticationManager {
    static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

    static {
        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Autowired
    private UserClient userClient;

    @Autowired
    public SampleManager(UserClient userClient){
        this.userClient = userClient;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        User target = userClient.findByUsername(auth.getName());
        if (target == null) {
            throw new RuntimeException(auth.getName());
        }
        if (BCrypt.checkpw((String) auth.getCredentials(), target.getPassword())) {
            return new UsernamePasswordAuthenticationToken(auth.getName(),
                    auth.getCredentials(), AUTHORITIES);
        }
        throw new BadCredentialsException("Bad Credentials");
    }
}