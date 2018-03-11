package com.trick.security.core.social.weixin.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trick.security.core.social.weixin.api.Weixin;
import com.trick.security.core.social.weixin.pojo.WeixinUserProfile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class WeixinImpl extends AbstractOAuth2ApiBinding implements Weixin {

    private static final String URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?openid=";

    private ObjectMapper objectMapper = new ObjectMapper();
    private String openid;

    public WeixinImpl(String accessToken) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    public WeixinImpl(String accessToken, String openid) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.openid = openid;
    }

    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(getFormMessageConverter());
        messageConverters.add(getJsonMessageConverter());
        messageConverters.add(getByteArrayMessageConverter());
        return messageConverters;
    }

    @Override
    public WeixinUserProfile getUserProfile() {
        String url = URL_GET_USER_INFO + openid;
        String result = getRestTemplate().getForObject(url, String.class);
        try {
            WeixinUserProfile profile = objectMapper.readValue(result, WeixinUserProfile.class);
            if (profile.getErrcode() != null) {
                throw new RuntimeException(profile.getErrmsg());
            }
            return profile;
        } catch (IOException e) {
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
}
