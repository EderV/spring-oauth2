package com.evm.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping
    public ResponseEntity<String> get() {
        log.info("Received GET request");
        return ResponseEntity.ok("Received");
    }

}
