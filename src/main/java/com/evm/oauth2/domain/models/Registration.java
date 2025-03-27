package com.evm.oauth2.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Registration {

    private String email;
    private String username;
    private String password;

}
