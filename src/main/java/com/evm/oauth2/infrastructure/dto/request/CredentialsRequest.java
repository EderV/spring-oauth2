package com.evm.oauth2.infrastructure.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CredentialsRequest {

    private String username;
    private String password;

}
