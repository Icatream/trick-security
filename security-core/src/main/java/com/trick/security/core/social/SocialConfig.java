package com.trick.security.core.social;

import com.trick.security.core.properties.SecurityProperties;
import com.trick.security.core.social.support.TrickSpringSocialConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    private final DataSource dataSource;
    private final SecurityProperties securityProperties;
    private Map<ConnectionFactoryLocator, UsersConnectionRepository> connectionMap = new HashMap<>();

    @Autowired
    public SocialConfig(DataSource dataSource, SecurityProperties securityProperties) {
        this.dataSource = dataSource;
        this.securityProperties = securityProperties;
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        UsersConnectionRepository repository = connectionMap.get(connectionFactoryLocator);
        if (repository == null) {
            repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
            connectionMap.put(connectionFactoryLocator, repository);
        }
        return repository;
    }

    @Bean
    public SpringSocialConfigurer trickSocialSecurityConfig() {
        SpringSocialConfigurer configurer = new TrickSpringSocialConfigurer(securityProperties.getSocial().getFilterProcessesUrl());
        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        return configurer;
    }

    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator, getUsersConnectionRepository(connectionFactoryLocator));
    }
}
