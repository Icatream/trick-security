package com.trick.security.core.varification.code;

import com.trick.security.core.properties.SecurityProperties;
import com.trick.security.core.varification.code.image.ImageCodeGenerator;
import com.trick.security.core.varification.code.sms.SmsCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VerificationBeanConfig {

    private final SecurityProperties securityProperties;

    @Autowired
    public VerificationBeanConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean
    @ConditionalOnMissingBean(ImageCodeGenerator.class)
    public VerificationCodeGenerator imageCodeGenerator() {
        return new ImageCodeGenerator(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean(SmsCodeGenerator.class)
    public SmsCodeGenerator smsCodeGenerator() {
        return new SmsCodeGenerator();
    }

}
