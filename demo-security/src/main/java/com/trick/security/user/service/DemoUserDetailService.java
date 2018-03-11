package com.trick.security.user.service;

import com.trick.security.user.dao.UserMapper;
import com.trick.security.user.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class DemoUserDetailService implements UserDetailsService, SocialUserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(DemoUserDetailService.class);
    private final UserMapper userMapper;

    @Autowired
    public DemoUserDetailService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public SocialUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = loadUser(username);
        return new SocialUser(user.getId(), user.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }

    private User loadUser(String username) {
        if (Pattern.matches("^\\d{11}$", username)) {
            return userMapper.selectByPhone(username);
        } else if (Pattern.matches("^([a-z0-9A-Z]+[-|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$", username)) {
            return userMapper.selectByEmail(username);
        } else {
            User user = userMapper.selectByUsername(username);
            if (user != null) {
                return user;
            }
            throw new RuntimeException("无效的用户名");
        }
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        User user = userMapper.selectById(userId);
        return new SocialUser(userId, user.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
