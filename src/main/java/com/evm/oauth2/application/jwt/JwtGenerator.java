package com.evm.oauth2.application.jwt;

import com.evm.oauth2.domain.interfaces.KeyGenerator;
import com.evm.oauth2.domain.interfaces.TokenGenerator;
import com.evm.oauth2.domain.models.AccessJwt;
import com.evm.oauth2.domain.models.User;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class JwtGenerator implements TokenGenerator<AccessJwt> {

    private final KeyGenerator keyGenerator;

    @Override
    public AccessJwt generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();
        Instant expiration = now.plus(60, ChronoUnit.MINUTES);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuedAt(now)
                .expiresAt(expiration)
                .issuer("main-app")
                // Additional claims can be added as needed:
                // .claim("roles", user.getRoles())
                .build();

        String token = jwtEncoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new AccessJwt(user.getId(), token);
    }

    private JwtEncoder jwtEncoder() {
        RSAPublicKey publicKey = (RSAPublicKey) keyGenerator.getPublicKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyGenerator.getPrivateKey();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        ImmutableJWKSet<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);

        return new NimbusJwtEncoder(jwkSource);
    }

}
