package com.trick.security.core.social.weixin.connect;

import com.trick.security.core.social.weixin.api.Weixin;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.AbstractConnection;
import org.springframework.social.oauth2.AccessGrant;

public class WeixinOAuth2Connection extends AbstractConnection<Weixin> {

    private transient final WeixinServiceProvider serviceProvider;
    private String accessToken;
    private String refreshToken;
    private Long expireTime;
    private transient Weixin api;

    public WeixinOAuth2Connection(String providerId, String providerUserId, String accessToken, String refreshToken, Long expireTime, WeixinServiceProvider serviceProvider, ApiAdapter<Weixin> apiAdapter) {
        super(apiAdapter);
        this.serviceProvider = serviceProvider;
        initAccessTokens(accessToken, refreshToken, expireTime);
        initApi();
        initKey(providerId, providerUserId);
    }

    public WeixinOAuth2Connection(ConnectionData data, WeixinServiceProvider serviceProvider, ApiAdapter<Weixin> apiAdapter) {
        super(data, apiAdapter);
        this.serviceProvider = serviceProvider;
        initAccessTokens(data.getAccessToken(), data.getRefreshToken(), data.getExpireTime());
        initApi();
    }

    public boolean hasExpired() {
        synchronized (getMonitor()) {
            return expireTime != null && System.currentTimeMillis() >= expireTime;
        }
    }

    public void refresh() {
        synchronized (getMonitor()) {
            AccessGrant accessGrant = serviceProvider.getOAuthOperations().refreshAccess(refreshToken, null);
            initAccessTokens(accessGrant.getAccessToken(), accessGrant.getRefreshToken(), accessGrant.getExpireTime());
            initApi();
        }
    }

    public Weixin getApi() {
        synchronized (getMonitor()) {
            return api;
        }
    }

    public ConnectionData createData() {
        synchronized (getMonitor()) {
            return new ConnectionData(getKey().getProviderId(), getKey().getProviderUserId(), getDisplayName(), getProfileUrl(), getImageUrl(), accessToken, null, refreshToken, expireTime);
        }
    }

    private void initAccessTokens(String accessToken, String refreshToken, Long expireTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expireTime = expireTime;
    }

    private void initApi() {
        api = serviceProvider.getApi(accessToken, getKey().getProviderUserId());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((accessToken == null) ? 0 : accessToken.hashCode());
        result = prime * result + ((expireTime == null) ? 0 : expireTime.hashCode());
        result = prime * result + ((refreshToken == null) ? 0 : refreshToken.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (WeixinOAuth2Connection.class != obj.getClass()) {
            return false;
        }
        WeixinOAuth2Connection other = (WeixinOAuth2Connection) obj;
        if (accessToken == null) {
            if (other.accessToken != null) {
                return false;
            }
        } else if (!accessToken.equals(other.accessToken)) {
            return false;
        }
        if (expireTime == null) {
            if (other.expireTime != null) return false;
        } else if (!expireTime.equals(other.expireTime)) {
            return false;
        }
        if (refreshToken == null) {
            return other.refreshToken == null;
        } else {
            return refreshToken.equals(other.refreshToken);
        }
    }
}
