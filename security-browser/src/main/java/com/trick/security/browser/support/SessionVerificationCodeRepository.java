package com.trick.security.browser.support;

import com.trick.security.core.varification.code.VerificationCodeRepository;
import com.trick.security.core.varification.code.VerificationCodeType;
import com.trick.security.core.varification.code.pojo.VerificationCode;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

@Component
public class SessionVerificationCodeRepository implements VerificationCodeRepository {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    private String sessionKey = "SESSION_KEY_FOR_CODE_";

    @Override
    public void save(ServletWebRequest request, VerificationCodeType type, VerificationCode verificationCode) {
        sessionStrategy.setAttribute(request, sessionKey + type, verificationCode);
    }

    @Override
    public VerificationCode get(ServletWebRequest request, VerificationCodeType type) {
        return (VerificationCode) sessionStrategy.getAttribute(request, sessionKey + type);
    }

    @Override
    public void remove(ServletWebRequest request, VerificationCodeType type) {
        sessionStrategy.removeAttribute(request, sessionKey + type);
    }
}
