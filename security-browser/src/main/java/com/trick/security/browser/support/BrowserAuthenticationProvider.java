package com.trick.security.browser.support;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

//@Component("browserAuthenticationProvider")
public class BrowserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        UserDetails user = userDetailsService.loadUserByUsername((String) token.getPrincipal());
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        } else if (passwordAuthenticate(token, user)) {
            throw new InternalAuthenticationServiceException("密码错误");
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean passwordAuthenticate(UsernamePasswordAuthenticationToken token, UserDetails user) {
        String savedPassword = user.getPassword();
        String tokenPassword = (String) token.getCredentials();
        return !StringUtils.equals(savedPassword, tokenPassword);
    }
}
