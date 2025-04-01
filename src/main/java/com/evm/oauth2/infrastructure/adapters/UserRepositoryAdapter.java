package com.evm.oauth2.infrastructure.adapters;

import com.evm.oauth2.domain.models.User;
import com.evm.oauth2.domain.ports.out.UserRepositoryPort;
import com.evm.oauth2.infrastructure.dto.entities.UserEntity;
import com.evm.oauth2.infrastructure.mappers.UserMapper;
import com.evm.oauth2.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        var userEntity = toUserEntity(user);
        var savedUser = userRepository.save(userEntity);
        return toUser(savedUser);
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
