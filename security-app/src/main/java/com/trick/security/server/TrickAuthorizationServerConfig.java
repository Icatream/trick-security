package com.trick.security.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class TrickAuthorizationServerConfig implements AuthorizationServerConfigurer {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    @Autowired
    public TrickAuthorizationServerConfig(
            DataSource dataSource
            , PasswordEncoder passwordEncoder
            , TokenStore tokenStore
            , AuthenticationManager authenticationManager
            , UserDetailsService userDetailsService) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("isAuthenticated()");
    }

    //TODO
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /*JdbcClientDetailsServiceBuilder b = clients.jdbc(dataSource)
                .passwordEncoder(passwordEncoder);
        ClientDetailsService service = b.build();*/
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();

        builder.withClient("trick")
                .secret("secret")
                .authorizedGrantTypes("refresh_token", "authorization_code", "password", "implicit")
                .scopes("all")
                .and()
                .withClient("trick1")
                .secret("secret1")
                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                .scopes("all")
                .and()
                .withClient("trick2")
                .secret("secret2")
                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);

        if (jwtTokenEnhancer != null && jwtAccessTokenConverter != null) {
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancers = new ArrayList<>();
            enhancers.add(jwtTokenEnhancer);
            enhancers.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(enhancers);
            endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
        }
    }
}
