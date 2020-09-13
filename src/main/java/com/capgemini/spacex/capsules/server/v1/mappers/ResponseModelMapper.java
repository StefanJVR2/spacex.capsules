package com.capgemini.spacex.capsules.server.v1.mappers;

import com.capgemini.spacex.capsules.client.models.CapsuleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseModelMapper {
    com.capgemini.spacex.capsules.repositories.models.Capsule convert(CapsuleResponse response);
}
