package com.trick.security.core.varification.code.pojo;

import org.springframework.security.core.SpringSecurityCoreVersion;

import java.io.Serializable;
import java.time.LocalDateTime;

public class VerificationCode implements Serializable {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private String code;

    private LocalDateTime expireTime;

    public VerificationCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public VerificationCode(String code, int expireSecond) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireSecond);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
