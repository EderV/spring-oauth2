package com.evm.oauth2.infrastructure.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @GetMapping()
    public String home(@AuthenticationPrincipal UserDetails userDetails) {
        return "Hello " + userDetails.getUsername() + ". You are an authenticated user";
    }

    @GetMapping("/admin")
    public String user(@AuthenticationPrincipal UserDetails userDetails) {
        return "Hello " + userDetails.getUsername() + ". You have ROLE_ADMIN!";
    }
}