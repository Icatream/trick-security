package com.trick.security.core.varification.code;

import com.trick.security.core.properties.SecurityConstants;
import com.trick.security.core.properties.SecurityProperties;
import com.trick.security.core.properties.verification.VerificationCodeProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class VerificationCodeFilter extends OncePerRequestFilter {

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    private Map<String, VerificationCodeType> uriMap = new HashMap<>();

    private final VerificationCodeProcessor verificationCodeProcessor;

    private final AuthenticationFailureHandler trickAuthenticationFailHandler;

    private final VerificationCodeProperties verificationCodeProperties;

    @Autowired
    public VerificationCodeFilter(VerificationCodeProcessor verificationCodeProcessor
            , AuthenticationFailureHandler trickAuthenticationFailHandler, SecurityProperties securityProperties) {
        this.verificationCodeProcessor = verificationCodeProcessor;
        this.trickAuthenticationFailHandler = trickAuthenticationFailHandler;
        this.verificationCodeProperties = securityProperties.getCode();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        VerificationCodeType type = getTypeFromRequest(request);
        if (type != null) {
            try {
                verificationCodeProcessor.verify(new ServletWebRequest(request, response), type);
                filterChain.doFilter(request, response);
            } catch (VerificationCodeException e) {
                trickAuthenticationFailHandler.onAuthenticationFailure(request, response, e);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
    //protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.initFilterBean();
        uriMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URI_FORM, VerificationCodeType.IMAGE);
        addPropertiesUri(verificationCodeProperties.getImage().getUri(), VerificationCodeType.IMAGE);
        addPropertiesUri(verificationCodeProperties.getSms().getUri(), VerificationCodeType.SMS);
    }

    private void addPropertiesUri(String uriString, VerificationCodeType type) {
        if (StringUtils.isNotBlank(uriString)) {
            String[] uris = StringUtils.splitByWholeSeparatorPreserveAllTokens(uriString, ",");
            for (String uri : uris) {
                uriMap.put(uri, type);
            }
        }
    }

    private VerificationCodeType getTypeFromRequest(HttpServletRequest request) {
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), "GET")) {
            Set<String> uris = uriMap.keySet();
            for (String uri : uris) {
                if (pathMatcher.match(uri, request.getRequestURI())) {
                    return uriMap.get(uri);
                }
            }
        }
        return null;
    }
}
