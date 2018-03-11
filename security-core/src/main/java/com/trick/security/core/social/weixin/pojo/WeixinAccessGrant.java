package com.trick.security.core.social.weixin.pojo;

import org.springframework.social.oauth2.AccessGrant;

public class WeixinAccessGrant extends AccessGrant {

    private String openid;

    public WeixinAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn, String openid) {
        super(accessToken, scope, refreshToken, expiresIn);
        this.openid = openid;
    }

    public WeixinAccessGrant(WeixinAccessToken token) {
        super(token.getAccess_token(), token.getScope(), token.getRefresh_token(), token.getExpires_in());
        this.openid = token.getOpenid();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
