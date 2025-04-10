package org.emangini.servolution.authz.config;


import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import static java.time.Duration.ofHours;
import static java.util.UUID.randomUUID;
import static org.emangini.servolution.authz.jose.Jwks.generateRsa;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.REFRESH_TOKEN;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.OPENID;
import static org.springframework.security.oauth2.server.authorization.client.RegisteredClient.withId;

@Slf4j
@Configuration(proxyBeanMethods = false)
@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthorizationServerConfiguration {

    @Bean
    public RegisteredClientRepository registeredClientRepository() {

        log.info("Registering OAuth Client. Allowing all grant flows...");

        // TODO clean this up.
        RegisteredClient writer = withId(randomUUID().toString())
                .clientId("writer")
                .clientSecret(createDelegatingPasswordEncoder().encode("writer"))
                .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                .authorizationGrantType(AUTHORIZATION_CODE)
                .authorizationGrantType(REFRESH_TOKEN)
                .authorizationGrantType(CLIENT_CREDENTIALS)
                .redirectUri("https://my.redirect.uri")
                .redirectUri("https://localhost:8443/webjars/swagger-ui/oauth2-redirect.html")
                .scope(OPENID)
                .scope("product:read")
                .scope("product:write")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(ofHours(1)).build())
                .build();


        RegisteredClient reader = withId(randomUUID().toString())
                .clientId("reader")
                .clientSecret(createDelegatingPasswordEncoder().encode("reader"))
                .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                .authorizationGrantType(AUTHORIZATION_CODE)
                .authorizationGrantType(REFRESH_TOKEN)
                .authorizationGrantType(CLIENT_CREDENTIALS)
                .redirectUri("https://my.redirect.uri")
                .redirectUri("https://localhost:8443/webjars/swagger-ui/oauth2-redirect.html")
                .scope(OPENID)
                .scope("product:read")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(ofHours(1)).build())
                .build();


        log.info("Client Settings: {}", reader.getClientSettings().toString());
        log.info("Scopes: {}", reader.getScopes().toString());
        log.info("Jwk Set URL: {}", reader.getClientSettings().getJwkSetUrl());
        log.info("Access Token Format: {}",reader.getTokenSettings().getAccessTokenFormat().toString());
        log.info("Token Settings: {}", reader.getTokenSettings().toString());
        log.info("ID Token SigAlg: {} ", reader.getTokenSettings().getIdTokenSignatureAlgorithm().toString());

        return new InMemoryRegisteredClientRepository(writer, reader);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://auth-server")
                .build();
    }


}
