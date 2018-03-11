package com.trick.security.browser.config;

import com.trick.security.core.properties.SecurityProperties;
import com.trick.security.core.properties.browser.BrowserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

@Component
public class RememberMeConfig {

    private final UserDetailsService userDetailsService;
    private final BrowserProperties browserProperties;
    private final PersistentTokenRepository persistentTokenRepository;

    @Autowired
    public RememberMeConfig(UserDetailsService userDetailsService, SecurityProperties securityProperties, PersistentTokenRepository persistentTokenRepository) {
        this.userDetailsService = userDetailsService;
        this.browserProperties = securityProperties.getBrowser();
        this.persistentTokenRepository = persistentTokenRepository;
    }

    public void config(RememberMeConfigurer<HttpSecurity> rememberMe) {
        rememberMe.tokenRepository(persistentTokenRepository)
                .tokenValiditySeconds(browserProperties.getRememberMeSeconds())
                .userDetailsService(userDetailsService);
    }

}
