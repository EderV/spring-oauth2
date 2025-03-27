package com.evm.oauth2.infrastructure.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    // Public endpoint
    @GetMapping("/home")
    public String home() {
        return "Welcome to the public homepage!";
    }

    // Secured endpoint: returns user attributes
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        // Spring Security injects the OAuth2User after successful authentication.
        return principal.getAttributes();
    }
}