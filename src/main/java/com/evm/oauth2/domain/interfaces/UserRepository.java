package com.evm.oauth2.domain.interfaces;

import com.evm.oauth2.domain.models.User;
import com.evm.oauth2.domain.models.responses.DatabaseResponse;

public interface UserRepository {

    DatabaseResponse<User> saveUser(User user);
    User getUserFromUsername(String username);
    User getUserFromEmail(String email);
    void deleteAll();

}
