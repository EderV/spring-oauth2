package com.evm.oauth2.infrastructure.security;

import com.evm.oauth2.domain.interfaces.KeyGenerator;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
@RequiredArgsConstructor
public class CustomJwtEncoder implements JwtEncoder {

    private final KeyGenerator keyGenerator;

    @Override
    public Jwt encode(JwtEncoderParameters parameters) throws JwtEncodingException {
        RSAPublicKey publicKey = (RSAPublicKey) keyGenerator.getPublicKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyGenerator.getPrivateKey();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        ImmutableJWKSet<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);

        var jwtEncoder = new NimbusJwtEncoder(jwkSource);
        return jwtEncoder.encode(parameters);
    }

}
