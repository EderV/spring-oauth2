package com.evm.oauth2.application.services;

import com.evm.oauth2.domain.exceptions.UserAlreadyRegisteredException;
import com.evm.oauth2.domain.interfaces.TokenGenerator;
import com.evm.oauth2.domain.models.*;
import com.evm.oauth2.domain.ports.in.AuthServicePort;
import com.evm.oauth2.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthServicePort {

    private final AuthenticationProvider authenticationProvider;
    private final TokenGenerator<AccessJwt> tokenGenerator;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AccessJwt login(Credentials credentials) throws AuthenticationException {
        var username = credentials.getUsername();
        var password = credentials.getPassword();

        var userDetails = new User();
        userDetails.setUsername(username);
        userDetails.setPassword(password);
        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        var authenticationAuthenticated = authenticationProvider.authenticate(authentication);
        return tokenGenerator.generateToken(authenticationAuthenticated);
    }

    @Override
    public void registerUser(Registration registration) throws UserAlreadyRegisteredException {
        var email = registration.getEmail();
        var username = registration.getUsername();
        var password = registration.getPassword();

        checkNewUser(email, username);

        var user = createUser(email, username, password);
        userRepositoryPort.saveUser(user);
    }

    @Override
    public User validateUserLocallyIssued(String username) {
        return userRepositoryPort.getUserFromUsername(username);
    }

    @Override
    public User validateUserExternallyIssued(String email, String username, String issuer) {
        // Check if the user is in the database
        var user = userRepositoryPort.getUserFromUsername(username);

        if (user != null) return user;

        var newUser = createUser(email, username, "");
        newUser.setLoginIssuer(issuer);
        return userRepositoryPort.saveUser(newUser);
    }

    private void checkNewUser(String email, String username) throws UserAlreadyRegisteredException {
        var userByEmail = userRepositoryPort.getUserFromEmail(email);
        if (userByEmail != null) {
            throw new UserAlreadyRegisteredException("Email " + email + " already exists");
        }

        var userByUsername = userRepositoryPort.getUserFromUsername(username);
        if (userByUsername != null) {
            throw new UserAlreadyRegisteredException("Username " + username + " already exists");
        }
    }

    private User createUser(String email, String username, String password) {
        var user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setLoginIssuer("main-app");
        user.setAccountEnabled(true);
        user.setAccountExpired(false);
        user.setAccountLocked(false);
        user.setCredentialsExpired(false);

        var role = new Role();
        role.setRole("ROLE_USER");
        role.setUser(user);

        user.setRoles(List.of(role));

        return user;
    }

}
