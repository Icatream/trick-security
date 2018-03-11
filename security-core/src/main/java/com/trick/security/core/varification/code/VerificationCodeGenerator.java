package com.trick.security.core.varification.code;

import com.trick.security.core.varification.code.pojo.VerificationCode;
import org.springframework.web.context.request.ServletWebRequest;

public interface VerificationCodeGenerator {

    void generateAndSend(ServletWebRequest request, VerificationCode verificationCode) throws Exception;

}
