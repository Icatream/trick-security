package com.trick.security.core.social.qq.config;

import com.trick.security.core.properties.SecurityProperties;
import com.trick.security.core.properties.social.QQProperties;
import com.trick.security.core.social.qq.api.QQ;
import com.trick.security.core.social.qq.connect.QQConnectionFactory;
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
@ConditionalOnProperty(prefix = "trick.security.social.qq", name = "app-id")
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

    private final QQProperties qqProperties;

    @Autowired
    public QQAutoConfig(SecurityProperties securityProperties) {
        this.qqProperties = securityProperties.getSocial().getQq();
    }

    @Override
    protected ConnectionFactory<QQ> createConnectionFactory() {
        return new QQConnectionFactory(qqProperties.getProviderId(), qqProperties.getAppId(), qqProperties.getAppSecret());
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }

    @Bean("connect/qqConnect")
    @ConditionalOnMissingBean(name = "qqConnectView")
    public View qqConnectView() {
        return new SimpleView("QQ解绑成功");
    }

    @Bean("connect/qqConnected")
    @ConditionalOnMissingBean(name = "qqConnectedView")
    public View qqConnectedView() {
        return new SimpleView("QQ绑定成功");
    }
}
