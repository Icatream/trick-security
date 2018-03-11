package com.trick.security.core.varification.code;

import com.trick.security.core.properties.SecurityProperties;
import com.trick.security.core.properties.verification.VerificationCodeProperties;
import com.trick.security.core.varification.code.pojo.VerificationCode;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

@Component
public class VerificationCodeProcessor {

    private final VerificationCodeProperties codeProperties;
    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationCodeGeneratorHolder verificationCodeGeneratorHolder;

    @Autowired
    public VerificationCodeProcessor(SecurityProperties securityProperties, VerificationCodeRepository verificationCodeRepository
            , VerificationCodeGeneratorHolder verificationCodeGeneratorHolder) {
        this.codeProperties = securityProperties.getCode();
        this.verificationCodeRepository = verificationCodeRepository;
        this.verificationCodeGeneratorHolder = verificationCodeGeneratorHolder;
    }

    public void create(ServletWebRequest request, VerificationCodeType type) throws Exception {
        VerificationCodeGenerator generator = verificationCodeGeneratorHolder.getGenerator(type);
        if (generator == null) {
            throw new VerificationCodeException(type + "验证码处理器不存在");
        }
        String code = RandomStringUtils.randomNumeric(codeProperties.getLength());
        VerificationCode verificationCode = new VerificationCode(code, codeProperties.getExpireSecond());
        generator.generateAndSend(request, verificationCode);
        verificationCodeRepository.save(request, type, verificationCode);
    }

    public void verify(ServletWebRequest request, VerificationCodeType type) {
        VerificationCode storedCode = verificationCodeRepository.get(request, type);
        if (storedCode == null) {
            throw new VerificationCodeException("请获取验证码后再发送登录请求");
        }
        String typeInfo = type.getInfo();
        if (storedCode.isExpired()) {
            throw new VerificationCodeException(typeInfo + "验证码已过期");
        }
        String requestCode;
        try {
            requestCode = ServletRequestUtils.getStringParameter(request.getRequest(), typeInfo);
        } catch (ServletRequestBindingException e) {
            throw new VerificationCodeException("获取验证码失败");
        }
        if (StringUtils.isBlank(requestCode)) {
            throw new VerificationCodeException(typeInfo + "验证码不能为空");
        }
        if (!StringUtils.equals(storedCode.getCode(), requestCode)) {
            throw new VerificationCodeException(typeInfo + "验证码不匹配");
        }
        verificationCodeRepository.remove(request, type);
    }
}
