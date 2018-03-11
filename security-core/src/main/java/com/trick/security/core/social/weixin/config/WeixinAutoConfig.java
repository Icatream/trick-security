package com.trick.security.core.social.weixin.config;

import com.trick.security.core.properties.SecurityProperties;
import com.trick.security.core.properties.social.WeixinProperties;
import com.trick.security.core.social.weixin.connect.WeixinConnectionFactory;
import com.trick.security.core.support.SimpleView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.web.servlet.View;

@Configuration
@ConditionalOnProperty(prefix = "trick.security.social.weixin", name = "app-id")
public class WeixinAutoConfig extends SocialAutoConfigurerAdapter {

    private WeixinProperties weixin;

    @Autowired
    public WeixinAutoConfig(SecurityProperties securityProperties) {
        this.weixin = securityProperties.getSocial().getWeixin();
    }

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        return new WeixinConnectionFactory(weixin.getProviderId(), weixin.getAppId(), weixin.getAppSecret());
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }

    @Bean("connect/weixinConnect")
    @ConditionalOnMissingBean(name = "weixinConnectView")
    public View weixinConnectView() {
        return new SimpleView("微信解绑成功");
    }

    @Bean("connect/weixinConnected")
    @ConditionalOnMissingBean(name = "weixinConnectedView")
    public View weixinConnectedView() {
        return new SimpleView("微信绑定成功");
    }
}
