package com.evm.oauth2.infrastructure.security;

import com.evm.oauth2.domain.interfaces.KeyGenerator;
import com.evm.oauth2.infrastructure.configuration.JwtIssuerConfig;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {

    private final JwtIssuerConfig jwtIssuerConfig;

    private final KeyGenerator keyGenerator;
    private final Map<String, JwtDecoder> decodersMap = new HashMap<>();

    @PostConstruct
    private void postConstruct() {
        var appDecoder = NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyGenerator.getPublicKey()).build();
        decodersMap.put(jwtIssuerConfig.getApp().getIssuerUri(), appDecoder);

        var googleJwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwtIssuerConfig.getGoogle().getJwkSetUri()).build();
        decodersMap.put(jwtIssuerConfig.getGoogle().getIssuerUri(), googleJwtDecoder);

        var facebookDecoder = NimbusJwtDecoder.withJwkSetUri(jwtIssuerConfig.getFacebook().getJwkSetUri()).build();
        decodersMap.put(jwtIssuerConfig.getFacebook().getIssuerUri(), facebookDecoder);
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        String issuer;
        try {
            JWTClaimsSet unverifiedJwt = JWTParser.parse(token).getJWTClaimsSet();
            issuer = unverifiedJwt.getIssuer();
        } catch (ParseException e) {
            throw new JwtException("Error parsing token. ".concat(token));
        }

        if (issuer == null || !decodersMap.containsKey(issuer)) {
            throw new JwtException("Issuer " + issuer + " not found");
        }

        return decodersMap.get(issuer).decode(token);
    }

}