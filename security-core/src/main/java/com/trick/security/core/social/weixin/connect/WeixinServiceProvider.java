package com.trick.security.core.social.weixin.connect;

import com.trick.security.core.social.weixin.api.Weixin;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

public interface WeixinServiceProvider extends OAuth2ServiceProvider<Weixin> {
    Weixin getApi(String accessToken, String openid);
}
