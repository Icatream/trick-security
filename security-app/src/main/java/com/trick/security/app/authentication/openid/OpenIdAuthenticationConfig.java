package com.trick.security.app.authentication.openid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class OpenIdAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final SocialUserDetailsService userDetailsService;
    private final UsersConnectionRepository usersConnectionRepository;
    private final AuthenticationSuccessHandler trickAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler trickAuthenticationFailHandler;

    @Autowired
    public OpenIdAuthenticationConfig(SocialUserDetailsService userDetailsService
            , UsersConnectionRepository usersConnectionRepository
            , AuthenticationSuccessHandler trickAuthenticationSuccessHandler
            , AuthenticationFailureHandler trickAuthenticationFailHandler) {
        this.userDetailsService = userDetailsService;
        this.usersConnectionRepository = usersConnectionRepository;
        this.trickAuthenticationSuccessHandler = trickAuthenticationSuccessHandler;
        this.trickAuthenticationFailHandler = trickAuthenticationFailHandler;
    }

    @Override
    public void configure(HttpSecurity builder) {
        OpenIdAuthenticationFilter filter = new OpenIdAuthenticationFilter();
        filter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        filter.setAuthenticationSuccessHandler(trickAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(trickAuthenticationFailHandler);
        OpenIdAuthenticationProvider provider = new OpenIdAuthenticationProvider(usersConnectionRepository, userDetailsService);
        builder.authenticationProvider(provider)
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
