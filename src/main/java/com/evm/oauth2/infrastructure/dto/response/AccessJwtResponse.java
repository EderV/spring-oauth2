package com.evm.oauth2.infrastructure.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessJwtResponse {

    private long userId;
    private String jwtToken;

}
