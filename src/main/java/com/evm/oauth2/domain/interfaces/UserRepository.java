package com.evm.oauth2.domain.interfaces;

import com.evm.oauth2.domain.models.User;

public interface UserRepository {

    User saveUser(User user);
    User getUserFromUsername(String username);
    User getUserFromEmail(String email);
    void deleteAll();

}
