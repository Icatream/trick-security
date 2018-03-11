package com.trick.security.app.authentication.openid;

import com.trick.security.core.properties.SecurityConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OpenIdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    protected OpenIdAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_OPENID, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String providerId = obtainParameter(request,SecurityConstants.DEFAULT_PARAMETER_NAME_PROVIDERID);
        String openId = obtainParameter(request, SecurityConstants.DEFAULT_PARAMETER_NAME_OPENID);
        OpenIdAuthenticationToken authRequest = new OpenIdAuthenticationToken(openId, providerId);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private String obtainParameter(HttpServletRequest request, String name) {
        String parameter = request.getParameter(name);
        return (parameter != null) ? parameter.trim() : "";
    }

    private void setDetails(HttpServletRequest request, OpenIdAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
