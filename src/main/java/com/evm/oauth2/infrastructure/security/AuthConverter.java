package com.evm.oauth2.infrastructure.security;

import com.evm.oauth2.domain.ports.in.AuthServicePort;
import com.evm.oauth2.infrastructure.configuration.JwtIssuerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final AuthServicePort authServicePort;
    private final JwtIssuerConfig jwtIssuerConfig;

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        throw new OAuth2AuthenticationException("aaaaaaaaaaaaaaaaaaa");

//        var issuer = (String) source.getClaims().get("iss");
//
//        if (issuer.equals(jwtIssuerConfig.getApp().getIssuerUri())) {
//            var user = authServicePort.validateUserLocallyIssued(source.getSubject());
//            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//        }
//
//        String email = (String) source.getClaims().getOrDefault("email", "");
//        String username = (String) source.getClaims().getOrDefault("name", "");
//
//        if (email.isBlank() || username.isBlank()) {
//            throw new OAuth2AuthenticationException(
//                    "Email or Name are invalid in the JWT. Email: " + email + " - Name: " + username);
//        }
//
//        var user = authServicePort.validateUserExternallyIssued(email, username, issuer);
//        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

}
