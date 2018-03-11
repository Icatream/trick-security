package com.trick.security.core.properties;

import com.trick.security.core.properties.browser.BrowserProperties;
import com.trick.security.core.properties.oauth2.OAuth2Properties;
import com.trick.security.core.properties.social.SocialProperties;
import com.trick.security.core.properties.verification.VerificationCodeProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "trick.security")
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();

    private VerificationCodeProperties code = new VerificationCodeProperties();

    private SocialProperties social = new SocialProperties();

    private OAuth2Properties oAuth2 = new OAuth2Properties();

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }

    public VerificationCodeProperties getCode() {
        return code;
    }

    public void setCode(VerificationCodeProperties code) {
        this.code = code;
    }

    public SocialProperties getSocial() {
        return social;
    }

    public void setSocial(SocialProperties social) {
        this.social = social;
    }

    public OAuth2Properties getOAuth2() {
        return oAuth2;
    }

    public void setOAuth2(OAuth2Properties oAuth2) {
        this.oAuth2 = oAuth2;
    }
}
