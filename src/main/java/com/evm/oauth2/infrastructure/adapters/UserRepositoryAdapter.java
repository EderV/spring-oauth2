package com.evm.oauth2.infrastructure.adapters;

import com.evm.oauth2.domain.interfaces.UserRepository;
import com.evm.oauth2.domain.models.User;
import com.evm.oauth2.domain.models.responses.DatabaseResponse;
import com.evm.oauth2.infrastructure.dto.entities.UserEntity;
import com.evm.oauth2.infrastructure.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final com.evm.oauth2.infrastructure.repository.UserRepository userRepository;

    @Override
    public DatabaseResponse<User> saveUser(User user) {
        var res = new DatabaseResponse<User>();

        var userEntity = toUserEntity(user);
        try {
            var savedUser = userRepository.save(userEntity);

            res.setData(toUser(savedUser));
        } catch (DataIntegrityViolationException ex) {
            res.setErrorMsg(ex.getMessage());
            res.setThrowable(ex);
        }

        return res;
    }

    @Override
    public User getUserFromUsername(String username) {
        var userEntity = userRepository.getUserByUsername(username);
        return userEntity.map(this::toUser).orElse(null);
    }

    @Override
    public User getUserFromEmail(String email) {
        var userEntity = userRepository.getUserByEmail(email);
        return userEntity.map(this::toUser).orElse(null);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    private User toUser(UserEntity userEntity) {
        return UserMapper.MAPPER.toUser(userEntity);
    }

    private UserEntity toUserEntity(User user) {
        return UserMapper.MAPPER.toUserEntity(user);
    }

}
