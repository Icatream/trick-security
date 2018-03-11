package com.trick.security.core.varification.code;

public enum VerificationCodeType {
    IMAGE, SMS;

    public String getInfo() {
        return this.name().toLowerCase() + "Code";
    }

    public String getGenerator() {
        return this.name().toLowerCase() + "CodeGenerator";
    }
}
