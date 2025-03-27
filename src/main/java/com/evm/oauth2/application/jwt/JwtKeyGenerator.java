package com.evm.oauth2.application.jwt;

import com.evm.oauth2.domain.interfaces.KeyGenerator;
import com.evm.oauth2.domain.ports.out.FileSystemPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtKeyGenerator implements KeyGenerator {

    private final String jwtKeysPath = "jwt-keys";
    private final String publicJwtKeyPath = "jwt-keys/public.key";
    private final String privateJwtKeyPath = "jwt-keys/private.key";

    private KeyPair accessTokenKeyPair;

    private final FileSystemPort fileSystemPort;

    @Override
    public Key getPublicKey() {
        return getAccessTokenKeyPair().getPublic();
    }

    @Override
    public Key getPrivateKey() {
        return getAccessTokenKeyPair().getPrivate();
    }

    private KeyPair getAccessTokenKeyPair() {
        if (Objects.isNull(accessTokenKeyPair)) {
            accessTokenKeyPair = getKeyPair(publicJwtKeyPath, privateJwtKeyPath);
        }
        return accessTokenKeyPair;
    }

    private KeyPair getKeyPair(String publicKeyPath, String privateKeyPath) {
        KeyPair keyPair;

        File publicKeyFile = fileSystemPort.newfile(publicKeyPath);
        File privateKeyFile = fileSystemPort.newfile(privateKeyPath);

        if (publicKeyFile.exists() && privateKeyFile.exists()) {
            log.info("Loading keys from file: {}, {}", publicKeyPath, privateKeyPath);
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                byte[] publicKeyBytes = fileSystemPort.readAllBytes(publicKeyFile.toPath());
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

                byte[] privateKeyBytes = fileSystemPort.readAllBytes(privateKeyFile.toPath());
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

                keyPair = new KeyPair(publicKey, privateKey);
                return keyPair;
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        File directory = fileSystemPort.newfile(jwtKeysPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            log.info("Generating new public and private keys: {}, {}", publicKeyPath, privateKeyPath);
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();

            try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                fos.write(keySpec.getEncoded());
            }

            try (FileOutputStream fos = new FileOutputStream(privateKeyPath)) {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
                fos.write(keySpec.getEncoded());
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }

        return keyPair;
    }

}
