package com.evm.oauth2.domain.ports.out;

import com.evm.oauth2.domain.models.User;

public interface UserRepositoryPort {

    User saveUser(User user);
    User getUserFromUsername(String username);
    User getUserFromEmail(String email);
    void deleteAll();

}
