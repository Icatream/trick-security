package com.trick.security.browser;

import com.trick.security.browser.config.RememberMeConfig;
import com.trick.security.browser.config.SessionConfig;
import com.trick.security.core.FormAuthenticationConfig;
import com.trick.security.core.authentication.sms.SmsCodeAuthenticationConfig;
import com.trick.security.core.authorization.AuthorizationManager;
import com.trick.security.core.varification.code.VerificationCodeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthorizationManager authorizationManager;
    private final VerificationCodeConfig verificationCodeConfig;
    private final SpringSocialConfigurer trickSocialSecurityConfig;
    private final FormAuthenticationConfig formAuthenticationConfig;
    private final RememberMeConfig rememberMeConfig;
    private final SessionConfig sessionConfig;
    private final SmsCodeAuthenticationConfig smsCodeAuthenticationConfig;

    @Autowired
    public BrowserSecurityConfig(AuthorizationManager authorizationManager, VerificationCodeConfig verificationCodeConfig
            , SpringSocialConfigurer trickSocialSecurityConfig, FormAuthenticationConfig formAuthenticationConfig
            , RememberMeConfig rememberMeConfig, SessionConfig sessionConfig, SmsCodeAuthenticationConfig smsCodeAuthenticationConfig) {
        this.authorizationManager = authorizationManager;
        this.verificationCodeConfig = verificationCodeConfig;
        this.trickSocialSecurityConfig = trickSocialSecurityConfig;
        this.formAuthenticationConfig = formAuthenticationConfig;
        this.rememberMeConfig = rememberMeConfig;
        this.sessionConfig = sessionConfig;
        this.smsCodeAuthenticationConfig = smsCodeAuthenticationConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        formAuthenticationConfig.config(http);
        rememberMeConfig.config(http.rememberMe());
        sessionConfig.config(http.sessionManagement());
        http.apply(verificationCodeConfig)
                .and()
                .apply(trickSocialSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationConfig)
                .and()
                .csrf().disable();
        authorizationManager.config(http.authorizeRequests());
    }

}
