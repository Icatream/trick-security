package com.trick.security.core.authentication.sms;

import com.trick.security.core.varification.code.VerificationCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SmsCodeAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final AuthenticationSuccessHandler trickAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler trickAuthenticationFailureHandler;
    private final UserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;
    private final VerificationCodeProcessor verificationCodeProcessor;

    @Autowired
    public SmsCodeAuthenticationConfig(
            AuthenticationSuccessHandler trickAuthenticationSuccessHandler, AuthenticationFailureHandler trickAuthenticationFailureHandler,
            UserDetailsService userDetailsService, PersistentTokenRepository persistentTokenRepository,
            VerificationCodeProcessor verificationCodeProcessor) {
        this.trickAuthenticationSuccessHandler = trickAuthenticationSuccessHandler;
        this.trickAuthenticationFailureHandler = trickAuthenticationFailureHandler;
        this.userDetailsService = userDetailsService;
        this.persistentTokenRepository = persistentTokenRepository;
        this.verificationCodeProcessor = verificationCodeProcessor;
    }

    @Override
    public void configure(HttpSecurity http) {
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter(verificationCodeProcessor, userDetailsService);
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(trickAuthenticationSuccessHandler);
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(trickAuthenticationFailureHandler);

        String key = UUID.randomUUID().toString();
        smsCodeAuthenticationFilter.setRememberMeServices(new PersistentTokenBasedRememberMeServices(key, userDetailsService, persistentTokenRepository));

        http.addFilterBefore(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
