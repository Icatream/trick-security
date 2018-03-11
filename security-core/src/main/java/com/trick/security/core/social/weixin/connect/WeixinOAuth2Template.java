package com.trick.security.core.social.weixin.connect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trick.security.core.social.weixin.pojo.WeixinAccessGrant;
import com.trick.security.core.social.weixin.pojo.WeixinAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;

public class WeixinOAuth2Template extends OAuth2Template {

    private static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    private static final String URL_AUTHORIZE = "https://open.weixin.qq.com/connect/qrconnect";
    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    private String clientId;
    private String clientSecret;
    private ObjectMapper objectMapper = new ObjectMapper();

    private static Logger logger = LoggerFactory.getLogger(WeixinOAuth2Template.class);

    public WeixinOAuth2Template(String clientId, String clientSecret) {
        super(clientId, clientSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN);
        setUseParametersForClientAuthentication(true);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public AccessGrant exchangeForAccess(String authorizationCode, String redirectUri, MultiValueMap<String, String> parameters) {
        String accessTokenRequestUrl = URL_ACCESS_TOKEN + "?appid=" + clientId +
                "&secret=" + clientSecret +
                "&code=" + authorizationCode +
                "&grant_type=authorization_code" +
                "&redirect_uri=" + redirectUri;
        return getAccessToken(accessTokenRequestUrl);
    }

    @Override
    public AccessGrant refreshAccess(String refreshToken, MultiValueMap<String, String> additionalParameters) {
        String refreshTokenUrl = REFRESH_TOKEN_URL + "?appid=" + clientId +
                "&grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;
        return getAccessToken(refreshTokenUrl);
    }

    private AccessGrant getAccessToken(String accessTokenRequestUrl) {
        logger.info("获取access_token, 请求URL: " + accessTokenRequestUrl);
        String result = getRestTemplate().getForObject(accessTokenRequestUrl, String.class);
        try {
            WeixinAccessToken token = objectMapper.readValue(result, WeixinAccessToken.class);
            logger.info("获取access_token, 响应内容: " + token);
            //返回错误码时直接返回空
            Integer errcode = token.getErrcode();
            if (errcode != null) {
                throw new RuntimeException("获取access_token失败, errcode:" + errcode + ", errmsg:" + token.getErrmsg());
            }
            return new WeixinAccessGrant(token);
        } catch (IOException e) {
            throw new RuntimeException("获取AccessToken失败", e);
        }
    }

    //构建获取授权码的请求。也就是引导用户跳转到微信的地址。
    @Override
    public String buildAuthenticateUrl(OAuth2Parameters parameters) {
        String url = super.buildAuthenticateUrl(parameters);
        return url + "&appid=" + clientId + "&scope=snsapi_login";
    }

    @Override
    public String buildAuthorizeUrl(OAuth2Parameters parameters) {
        return buildAuthenticateUrl(parameters);
    }

    //微信返回的contentType是html/text，添加相应的HttpMessageConverter来处理。
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }
}
