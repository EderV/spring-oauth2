package com.evm.oauth2.infrastructure.mappers;

import com.evm.oauth2.domain.models.Registration;
import com.evm.oauth2.infrastructure.dto.request.RegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {

    RegistrationMapper MAPPER = Mappers.getMapper(RegistrationMapper.class);

    Registration toRegistration(RegistrationRequest registrationRequest);

}
