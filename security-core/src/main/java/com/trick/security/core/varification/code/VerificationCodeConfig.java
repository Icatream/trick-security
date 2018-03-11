package com.trick.security.core.varification.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

@Component
public class VerificationCodeConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final Filter verificationCodeFilter;

    @Autowired
    public VerificationCodeConfig(Filter verificationCodeFilter) {
        this.verificationCodeFilter = verificationCodeFilter;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        httpSecurity.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
