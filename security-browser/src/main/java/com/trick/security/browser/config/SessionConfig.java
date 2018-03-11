package com.trick.security.browser.config;

import com.trick.security.browser.support.DefaultSessionExpiredStrategy;
import com.trick.security.core.properties.SecurityProperties;
import com.trick.security.core.properties.browser.session.SessionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.web.session.SimpleRedirectInvalidSessionStrategy;
import org.springframework.stereotype.Component;

@Component
public class SessionConfig {

    private final SessionProperties sessionProperties;

    @Autowired
    public SessionConfig(SecurityProperties securityProperties) {
        this.sessionProperties = securityProperties.getBrowser().getSession();
    }

    public void config(SessionManagementConfigurer<HttpSecurity> http) {
        http.invalidSessionStrategy(new SimpleRedirectInvalidSessionStrategy(sessionProperties.getSessionInvalidUri()))
                .maximumSessions(sessionProperties.getMaximumSessions())
                .maxSessionsPreventsLogin(sessionProperties.isMaxSessionsPreventsLogin())
                .expiredSessionStrategy(new DefaultSessionExpiredStrategy());
    }
}
