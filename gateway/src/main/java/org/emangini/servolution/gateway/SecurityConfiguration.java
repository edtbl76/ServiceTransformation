package org.emangini.servolution.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@EnableWebFluxSecurity
@Configuration
public class SecurityConfiguration {

    // TODO solve deprecation
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http
                .csrf(CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("/headerrouting/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/oauth2/**").permitAll()
                        .pathMatchers("/login/**").permitAll()
                        .pathMatchers("/error/**").permitAll()
                        .pathMatchers("/openapi/**").permitAll()
                        .pathMatchers("/webjars/**").permitAll()
                        .pathMatchers("/config/**").permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(withDefaults()));



        return http.build();
    }
}
