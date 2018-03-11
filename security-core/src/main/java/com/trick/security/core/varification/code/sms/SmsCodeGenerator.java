package com.trick.security.core.varification.code.sms;

import com.trick.security.core.properties.SecurityConstants;
import com.trick.security.core.varification.code.VerificationCodeGenerator;
import com.trick.security.core.varification.code.pojo.VerificationCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

public class SmsCodeGenerator implements VerificationCodeGenerator {

    private static Logger logger = LoggerFactory.getLogger(SmsCodeGenerator.class);

    @Override
    public void generateAndSend(ServletWebRequest request, VerificationCode verificationCode) throws ServletRequestBindingException, IOException {
        String phone = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), SecurityConstants.DEFAULT_PARAMETER_NAME_PHONE);
        String code = verificationCode.getCode();
        request.getResponse().getWriter().write(code);
        logger.info("\nphone number: " + phone + "\ncode :" + code);
    }
}
