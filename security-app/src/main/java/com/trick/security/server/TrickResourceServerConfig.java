package com.trick.security.server;

import com.trick.security.app.authentication.openid.OpenIdAuthenticationConfig;
import com.trick.security.core.FormAuthenticationConfig;
import com.trick.security.core.authentication.sms.SmsCodeAuthenticationConfig;
import com.trick.security.core.authorization.AuthorizationManager;
import com.trick.security.core.varification.code.VerificationCodeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableResourceServer
public class TrickResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final AuthorizationManager authorizationManager;
    private final FormAuthenticationConfig formAuthenticationConfig;
    private final VerificationCodeConfig verificationCodeConfig;
    private final SmsCodeAuthenticationConfig smsCodeAuthenticationConfig;
    private final SpringSocialConfigurer trickSocialSecurityConfig;
    private final OpenIdAuthenticationConfig openIdAuthenticationConfig;

    @Autowired
    public TrickResourceServerConfig(AuthorizationManager authorizationManager
            , FormAuthenticationConfig formAuthenticationConfig
            , VerificationCodeConfig verificationCodeConfig
            , SmsCodeAuthenticationConfig smsCodeAuthenticationConfig
            , SpringSocialConfigurer trickSocialSecurityConfig
            , OpenIdAuthenticationConfig openIdAuthenticationConfig) {
        this.authorizationManager = authorizationManager;
        this.formAuthenticationConfig = formAuthenticationConfig;
        this.verificationCodeConfig = verificationCodeConfig;
        this.smsCodeAuthenticationConfig = smsCodeAuthenticationConfig;
        this.trickSocialSecurityConfig = trickSocialSecurityConfig;
        this.openIdAuthenticationConfig = openIdAuthenticationConfig;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        formAuthenticationConfig.config(http);
        http.apply(verificationCodeConfig)
                .and()
                .apply(smsCodeAuthenticationConfig)
                .and()
                .apply(trickSocialSecurityConfig)
                .and()
                .apply(openIdAuthenticationConfig)
                .and()
                .csrf().disable();
        authorizationManager.config(http.authorizeRequests());
    }
}
