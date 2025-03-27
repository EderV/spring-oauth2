package com.evm.oauth2.infrastructure.mappers;

import com.evm.oauth2.domain.models.Credentials;
import com.evm.oauth2.infrastructure.dto.request.CredentialsRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

    CredentialsMapper MAPPER = Mappers.getMapper(CredentialsMapper.class);

    Credentials toCredentials(CredentialsRequest credentialsRequest);

}
