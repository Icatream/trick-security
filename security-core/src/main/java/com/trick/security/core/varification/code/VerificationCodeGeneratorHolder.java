package com.trick.security.core.varification.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VerificationCodeGeneratorHolder {

    private final Map<String, VerificationCodeGenerator> verificationCodeGenerators;

    @Autowired
    public VerificationCodeGeneratorHolder(Map<String, VerificationCodeGenerator> verificationCodeGenerators) {
        this.verificationCodeGenerators = verificationCodeGenerators;
    }

    public VerificationCodeGenerator getGenerator(String name) {
        return verificationCodeGenerators.get(name);
    }

    public VerificationCodeGenerator getGenerator(VerificationCodeType type) {
        return verificationCodeGenerators.get(type.getGenerator());
    }

}
