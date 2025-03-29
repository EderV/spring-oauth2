package com.evm.oauth2.application.jwt;

import com.evm.oauth2.domain.interfaces.TokenGenerator;
import com.evm.oauth2.domain.models.AccessJwt;
import com.evm.oauth2.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class JwtGenerator implements TokenGenerator<AccessJwt> {

    private final JwtEncoder jwtEncoder;

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
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new AccessJwt(user.getId(), token);
    }

}
