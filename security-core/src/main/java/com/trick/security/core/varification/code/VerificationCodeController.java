package com.trick.security.core.varification.code;

import com.trick.security.core.properties.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class VerificationCodeController {

    private final VerificationCodeProcessor verificationCodeProcessor;
    private final AuthenticationFailureHandler trickAuthenticationFailHandler;

    @Autowired
    public VerificationCodeController(VerificationCodeProcessor verificationCodeProcessor
            , AuthenticationFailureHandler trickAuthenticationFailHandler) {
        this.verificationCodeProcessor = verificationCodeProcessor;
        this.trickAuthenticationFailHandler = trickAuthenticationFailHandler;
    }

    @GetMapping(SecurityConstants.DEFAULT_VERIFICATION_CODE_URI_PREFIX + "/{type}")
    public void createVerificationCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
        VerificationCodeType generator = Enum.valueOf(VerificationCodeType.class, type.toUpperCase());
        try {
            verificationCodeProcessor.create(new ServletWebRequest(request, response), generator);
        } catch (VerificationCodeException e) {
            trickAuthenticationFailHandler.onAuthenticationFailure(request, response, e);
        }
    }

}
