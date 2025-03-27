package com.evm.oauth2.infrastructure.mappers;

import com.evm.oauth2.domain.models.Role;
import com.evm.oauth2.domain.models.User;
import com.evm.oauth2.infrastructure.dto.entities.RoleEntity;
import com.evm.oauth2.infrastructure.dto.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

//    @Mapping(target = "roles", expression = "java(mapRoles(userEntity))")
    User toUser(UserEntity userEntity);

//    @Mapping(target = "roles", expression = "java(mapRoleEntities(user))")
//    UserEntity toUserEntity(User user);

    @Mappings({
            @Mapping(target = "user.roles", ignore = true)
    })
    Role toRole(RoleEntity roleEntity);
//
//    @Mappings({
//            @Mapping(target = "user.roles", ignore = true)
//    })
//    RoleEntity toRoleEntity(Role role);

//    default Role toRole(RoleEntity roleEntity) {
//        var role = new Role();
//        var user = new User();
//        user.setId(roleEntity.getUser().getId());
//        role.setId(roleEntity.getId());
//        role.setUser(user);
//        role.setRole(roleEntity.getRole());
//        role.setCreatedAt(roleEntity.getCreatedAt());
//        return role;
//    }
//
//    default RoleEntity toRoleEntity(Role role) {
//        var roleEntity = new RoleEntity();
//        var userEntity = new UserEntity();
//        userEntity.setId(role.getUser().getId());
//        roleEntity.setId(role.getId());
//        roleEntity.setUser(userEntity);
//        roleEntity.setRole(role.getRole());
//        roleEntity.setCreatedAt(role.getCreatedAt());
//        return roleEntity;
//    }

//    default List<Role> mapRoles(UserEntity userEntity) {
//        var userWithId = User.builder().id(userEntity.getId()).build();
//        var roles = new ArrayList<Role>();
//        for (var roleEntity : userEntity.getRoles()) {
//            var role = new Role();
//            role.setUser(userWithId);
//            role.setRole(roleEntity.getRole());
//            roles.add(role);
//        }
//        return roles;
//    }
//
    default UserEntity toUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        // Map basic properties
        userEntity.setEmail(user.getEmail());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setLoginIssuer(user.getLoginIssuer());
        userEntity.setAccountEnabled(user.isAccountEnabled());
        userEntity.setAccountExpired(user.isAccountExpired());
        userEntity.setAccountLocked(user.isAccountLocked());
        userEntity.setCredentialsExpired(user.isCredentialsExpired());
        userEntity.setCreatedAt(user.getCreatedAt());
        userEntity.setUpdatedAt(user.getUpdatedAt());

        List<RoleEntity> roleEntities = user.getRoles().stream().map(role -> {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setRole(role.getRole());
            roleEntity.setCreatedAt(role.getCreatedAt());
            roleEntity.setUser(userEntity);
            return roleEntity;
        }).toList();
        userEntity.setRoles(roleEntities);

        return userEntity;
    }

}
