package com.trick.security.core.varification.code;

import com.trick.security.core.varification.code.pojo.VerificationCode;
import org.springframework.web.context.request.ServletWebRequest;

public interface VerificationCodeRepository {

    void save(ServletWebRequest request, VerificationCodeType type, VerificationCode verificationCode);

    VerificationCode get(ServletWebRequest request, VerificationCodeType type);

    void remove(ServletWebRequest request, VerificationCodeType type);
}
