package com.trick.security.user.controller;

import com.trick.security.user.dao.UserMapper;
import com.trick.security.user.pojo.SocialUserInfo;
import com.trick.security.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final ProviderSignInUtils providerSignInUtils;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(ProviderSignInUtils providerSignInUtils, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.providerSignInUtils = providerSignInUtils;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/me")
    public Object getCurrentUser(@AuthenticationPrincipal UserDetails user) {
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        UserDetails user = (UserDetails) principal;*/
        return user;
    }

    @PostMapping("/register")
    public void register(User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = UUID.randomUUID().toString();
        String password = passwordEncoder.encode(user.getPassword());
        user.setId(userId);
        user.setPassword(password);
        userMapper.insert(user);
        providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));
        redirectStrategy.sendRedirect(request, response, "/user/me");
    }

    @GetMapping("/social-info")
    public SocialUserInfo getSocialUserInfo(HttpServletRequest request) {
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
        if (connection != null) {
            SocialUserInfo userInfo = new SocialUserInfo();
            userInfo.setProviderId(connection.getKey().getProviderId());
            userInfo.setProviderUserId(connection.getKey().getProviderUserId());
            userInfo.setNickname(connection.getDisplayName());
            userInfo.setImageUrl(connection.getImageUrl());
            return userInfo;
        }
        return null;
    }
}
