package com.trick.security.core.varification.code;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityCoreVersion;

public class VerificationCodeException extends AuthenticationException {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public VerificationCodeException(String msg) {
        super(msg);
    }
}
