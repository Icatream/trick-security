package com.trick.security.server;

import com.trick.security.app.support.DefaultJwtTokenEnhancer;
import com.trick.security.core.properties.SecurityProperties;
import com.trick.security.core.properties.oauth2.OAuth2Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Component;

@Component
public class TokenStoreConfig {

    @Configuration
    @ConditionalOnProperty(prefix = "trick.security.oAuth2", name = "tokenStore", havingValue = "redis", matchIfMissing = true)
    public static class RedisTokenConfig {
        private final RedisConnectionFactory redisConnectionFactory;

        @Autowired
        public RedisTokenConfig(RedisConnectionFactory redisConnectionFactory) {
            this.redisConnectionFactory = redisConnectionFactory;
        }

        @Bean
        public TokenStore tokenStore() {
            return new RedisTokenStore(redisConnectionFactory);
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = "trick.security.oAuth2", name = "tokenStore", havingValue = "jwt")
    public static class JwtTokenConfig {
        private final OAuth2Properties OAuth2Properties;

        @Autowired
        public JwtTokenConfig(SecurityProperties securityProperties) {
            this.OAuth2Properties = securityProperties.getOAuth2();
        }

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setSigningKey(OAuth2Properties.getJwtSigningKey());
            return converter;
        }

        @Bean
        public TokenStore tokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        @Bean
        public TokenEnhancer jwtTokenEnhancer() {
            return new DefaultJwtTokenEnhancer();
        }
    }
}
