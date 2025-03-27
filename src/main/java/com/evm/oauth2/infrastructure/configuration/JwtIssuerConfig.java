package com.evm.oauth2.infrastructure.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver.jwt")
public class JwtIssuerConfig {

    private Issuer app;
    private Issuer google;
    private Issuer facebook;

    @Data
    public static class Issuer {
        private String issuerUri;
        private String jwkSetUri;
    }

}
