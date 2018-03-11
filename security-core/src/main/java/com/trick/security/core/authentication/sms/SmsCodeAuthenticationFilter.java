package com.trick.security.core.authentication.sms;

import com.trick.security.core.properties.SecurityConstants;
import com.trick.security.core.varification.code.VerificationCodeProcessor;
import com.trick.security.core.varification.code.VerificationCodeType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final VerificationCodeProcessor verificationCodeProcessor;
    private final UserDetailsService userDetailsService;

    public SmsCodeAuthenticationFilter(VerificationCodeProcessor verificationCodeProcessor
            , UserDetailsService userDetailsService) {
        super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URI_SMS, "POST"));
        this.verificationCodeProcessor = verificationCodeProcessor;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        verificationCodeProcessor.verify(new ServletWebRequest(request, response), VerificationCodeType.SMS);
        String phone = request.getParameter(SecurityConstants.DEFAULT_PARAMETER_NAME_PHONE);
        UserDetails user = userDetailsService.loadUserByUsername(phone);
        SmsCodeAuthenticationToken token = new SmsCodeAuthenticationToken(user, user.getAuthorities());
        token.setDetails(authenticationDetailsSource.buildDetails(request));
        return token;
    }

}