package com.evm.oauth2.infrastructure.controllers;

import com.evm.oauth2.domain.models.AccessJwt;
import com.evm.oauth2.domain.models.Credentials;
import com.evm.oauth2.domain.models.Registration;
import com.evm.oauth2.domain.ports.in.AuthServicePort;
import com.evm.oauth2.infrastructure.dto.request.CredentialsRequest;
import com.evm.oauth2.infrastructure.dto.request.RegistrationRequest;
import com.evm.oauth2.infrastructure.dto.response.AccessJwtResponse;
import com.evm.oauth2.infrastructure.mappers.AccessJwtMapper;
import com.evm.oauth2.infrastructure.mappers.CredentialsMapper;
import com.evm.oauth2.infrastructure.mappers.RegistrationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServicePort authServicePort;

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test OK");
    }

    @PostMapping("/login")
    public ResponseEntity<AccessJwtResponse> login(@RequestBody CredentialsRequest credentialsRequest)
            throws IllegalArgumentException, AuthenticationException {

        var credentials = toCredentials(credentialsRequest);

        var accessJwt = authServicePort.login(credentials);
        var response = toAccessJwtResponse(accessJwt);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest registrationRequest)
            throws IllegalArgumentException {

        var registration = toRegistration(registrationRequest);

        authServicePort.registerUser(registration);

        return new ResponseEntity<>("User registered in DB", HttpStatus.CREATED);
    }

    private Credentials toCredentials(CredentialsRequest credentialsRequest) {
        return CredentialsMapper.MAPPER.toCredentials(credentialsRequest);
    }

    private AccessJwtResponse toAccessJwtResponse(AccessJwt token) {
        return AccessJwtMapper.MAPPER.toResponse(token);
    }

    private Registration toRegistration(RegistrationRequest registrationRequest) {
        return RegistrationMapper.MAPPER.toRegistration(registrationRequest);
    }

}
