package com.evm.oauth2.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AccessJwt {

    private long userId;
    private String jwtToken;

}
