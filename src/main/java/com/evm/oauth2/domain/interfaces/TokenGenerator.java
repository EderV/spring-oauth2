package com.evm.oauth2.domain.interfaces;

import org.springframework.security.core.Authentication;

public interface TokenGenerator<T> {

    T generateToken(Authentication authentication);

}
