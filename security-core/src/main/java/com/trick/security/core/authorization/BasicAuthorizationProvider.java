package com.trick.security.core.authorization;

import com.trick.security.core.properties.SecurityConstants;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Component
@Order
public class BasicAuthorizationProvider implements AuthorizationProvider {
    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests) {
        authorizeRequests.antMatchers(SecurityConstants.DEFAULT_PUBLIC_RESOURCES,
                SecurityConstants.DEFAULT_VERIFICATION_CODE_URI_PREFIX + "/*",
                SecurityConstants.DEFAULT_UNAUTHENTICATED_URI,
                SecurityConstants.DEFAULT_LOGIN_PROCESSING_URI_FORM,
                SecurityConstants.DEFAULT_LOGIN_PROCESSING_URI_SMS,
                SecurityConstants.DEFAULT_SESSION_INVALID_URI)
                .permitAll()
                .anyRequest()
                .authenticated();
    }
}
