package com.trick.security.core;

import com.trick.security.core.properties.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class FormAuthenticationConfig {

    private final AuthenticationSuccessHandler trickAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler trickAuthenticationFailHandler;
    private final LogoutSuccessHandler trickLogoutSuccessHandler;

    @Autowired
    public FormAuthenticationConfig(AuthenticationSuccessHandler trickAuthenticationSuccessHandler
            , AuthenticationFailureHandler trickAuthenticationFailHandler, LogoutSuccessHandler trickLogoutSuccessHandler) {
        this.trickAuthenticationSuccessHandler = trickAuthenticationSuccessHandler;
        this.trickAuthenticationFailHandler = trickAuthenticationFailHandler;
        this.trickLogoutSuccessHandler = trickLogoutSuccessHandler;
    }

    public void config(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATED_URI)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URI_FORM)
                .successHandler(trickAuthenticationSuccessHandler)
                .failureHandler(trickAuthenticationFailHandler)
                .and()
                .logout()
                .logoutUrl(SecurityConstants.DEFAULT_LOGOUT_URI)
                .logoutSuccessHandler(trickLogoutSuccessHandler)
                .deleteCookies("JSESSIONID");
    }
}
