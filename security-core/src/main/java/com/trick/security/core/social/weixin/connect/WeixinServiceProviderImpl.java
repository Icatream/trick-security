package com.trick.security.core.social.weixin.connect;

import com.trick.security.core.social.weixin.api.Weixin;
import com.trick.security.core.social.weixin.api.impl.WeixinImpl;
import org.springframework.social.oauth2.OAuth2Operations;

public class WeixinServiceProviderImpl implements WeixinServiceProvider {

    private final OAuth2Operations oauth2Operations;

    public WeixinServiceProviderImpl(String appId, String appSecret) {
        this.oauth2Operations = new WeixinOAuth2Template(appId, appSecret);
    }

    @Override
    public Weixin getApi(String accessToken) {
        return new WeixinImpl(accessToken);
    }

    @Override
    public Weixin getApi(String accessToken, String openid) {
        return new WeixinImpl(accessToken, openid);
    }

    @Override
    public final OAuth2Operations getOAuthOperations() {
        return oauth2Operations;
    }
}
