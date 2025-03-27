package com.evm.oauth2.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;

@Getter
@Setter
public class Role implements GrantedAuthority {

    private Long id;

    private User user;

    private String role;

    private Date createdAt;

    @Override
    public String getAuthority() {
        return role;
    }
}
