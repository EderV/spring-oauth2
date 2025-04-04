package com.evm.oauth2.domain.interfaces;

import com.evm.oauth2.domain.exceptions.UserAlreadyRegisteredException;
import com.evm.oauth2.domain.models.AccessJwt;
import com.evm.oauth2.domain.models.Credentials;
import com.evm.oauth2.domain.models.Registration;
import com.evm.oauth2.domain.models.User;
import com.evm.oauth2.domain.models.responses.DatabaseResponse;
import org.springframework.security.core.AuthenticationException;

public interface AuthService {

    AccessJwt login(Credentials credentials) throws AuthenticationException;
    DatabaseResponse<User> registerUser(Registration registration) throws UserAlreadyRegisteredException;
    User validateUserLocallyIssued(String username);
    DatabaseResponse<User> validateUserExternallyIssued(String email, String username, String issuer);

}
