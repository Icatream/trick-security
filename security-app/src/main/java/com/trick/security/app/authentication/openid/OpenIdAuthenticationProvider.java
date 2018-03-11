package com.trick.security.app.authentication.openid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialUserDetailsService;

import java.util.HashSet;
import java.util.Set;

public class OpenIdAuthenticationProvider implements AuthenticationProvider {

    private final UsersConnectionRepository usersConnectionRepository;
    private final SocialUserDetailsService userDetailsService;

    public OpenIdAuthenticationProvider(UsersConnectionRepository usersConnectionRepository, SocialUserDetailsService userDetailsService) {
        this.usersConnectionRepository = usersConnectionRepository;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OpenIdAuthenticationToken authenticationToken = (OpenIdAuthenticationToken) authentication;
        String providerId = authenticationToken.getProviderId();
        String openId = (String) authenticationToken.getPrincipal();
        Set<String> openIdSet = new HashSet<>();
        openIdSet.add(openId);
        Set<String> userIds = usersConnectionRepository.findUserIdsConnectedTo(providerId, openIdSet);
        if (CollectionUtils.isEmpty(userIds) || userIds.size() != 1) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        String userId = userIds.iterator().next();
        UserDetails user = userDetailsService.loadUserByUserId(userId);
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        OpenIdAuthenticationToken authenticationResult = new OpenIdAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OpenIdAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
