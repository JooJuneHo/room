package com.sns.room.global.jwt;

import com.sns.room.user.entity.User;
import com.sns.room.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Configuration
public class UserDetailsServiceImpl {

    public UserDetails getUser(Long userId, String username, UserRoleEnum role)
        throws UsernameNotFoundException {
        User user = new User(userId, username, role);
        return new UserDetailsImpl(user);
    }
}
