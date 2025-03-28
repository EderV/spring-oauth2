package com.evm.oauth2.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtDecoder jwtDecoder;
    private final Converter<Jwt, AbstractAuthenticationToken> jwtConverter;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/home", "/error").permitAll()
                        .requestMatchers("/api/auth/private").hasAnyRole("ADMIN")
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2 -> {
                            oauth2.jwt(jwtConfigurer -> {
                                        jwtConfigurer.decoder(jwtDecoder);
                                        jwtConfigurer.jwtAuthenticationConverter(jwtConverter);
                                    }
                            );
                            oauth2.withObjectPostProcessor(new ObjectPostProcessor<BearerTokenAuthenticationFilter>() {
                                @Override
                                public <O extends BearerTokenAuthenticationFilter> O postProcess(O object) {
                                    object.setAuthenticationFailureHandler(authenticationEntryPoint::commence);
                                    return object;
                                }
                            });
                        }
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
