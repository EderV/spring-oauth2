package com.evm.oauth2.infrastructure.mappers;

import com.evm.oauth2.domain.models.AccessJwt;
import com.evm.oauth2.infrastructure.dto.response.AccessJwtResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccessJwtMapper {

    AccessJwtMapper MAPPER = Mappers.getMapper(AccessJwtMapper.class);

    AccessJwtResponse toResponse(AccessJwt accessJwt);

}
