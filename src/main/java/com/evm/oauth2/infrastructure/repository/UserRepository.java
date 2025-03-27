package com.evm.oauth2.infrastructure.repository;

import com.evm.oauth2.infrastructure.dto.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u " +
            "LEFT JOIN FETCH u.roles r " +
            "WHERE u.username = ?1")
    Optional<UserEntity> getUserByUsername(String username);

    @Query("SELECT u FROM UserEntity u " +
            "LEFT JOIN FETCH u.roles r " +
            "WHERE u.email = ?1")
    Optional<UserEntity> getUserByEmail(String email);

}
