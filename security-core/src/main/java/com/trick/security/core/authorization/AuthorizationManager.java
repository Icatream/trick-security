package com.trick.security.core.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorizationManager {

    private final List<AuthorizationProvider> authorizationProviders;

    @Autowired
    public AuthorizationManager(List<AuthorizationProvider> authorizationProviders) {
        this.authorizationProviders = authorizationProviders;
    }

    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests) {
        for (AuthorizationProvider provider : authorizationProviders) {
            provider.config(authorizeRequests);
        }
    }
}
