package com.evm.oauth2.domain.interfaces;

import java.security.Key;

public interface KeyGenerator {

    Key getPublicKey();
    Key getPrivateKey();

}
