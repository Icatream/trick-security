package com.trick.security.core.social.weixin.connect;

import com.trick.security.core.social.weixin.api.Weixin;
import com.trick.security.core.social.weixin.pojo.WeixinAccessGrant;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;

public class WeixinConnectionFactory extends OAuth2ConnectionFactory<Weixin> {

    public WeixinConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new WeixinServiceProviderImpl(appId, appSecret), new WeixinAdapter());
    }

    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if (accessGrant instanceof WeixinAccessGrant) {
            WeixinAccessGrant weixinAccessGrant = (WeixinAccessGrant) accessGrant;
            return weixinAccessGrant.getOpenid();
        }
        return null;
    }

    @Override
    public Connection<Weixin> createConnection(AccessGrant accessGrant) {
        return new WeixinOAuth2Connection(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getWeixinServiceProvider(), getApiAdapter());
    }

    @Override
    public Connection<Weixin> createConnection(ConnectionData data) {
        return new WeixinOAuth2Connection(data, getWeixinServiceProvider(), getApiAdapter());
    }

    private WeixinServiceProvider getWeixinServiceProvider() {
        return (WeixinServiceProvider) getServiceProvider();
    }
}
