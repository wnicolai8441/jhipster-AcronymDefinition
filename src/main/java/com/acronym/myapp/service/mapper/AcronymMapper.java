package com.acronym.myapp.service.mapper;

import com.acronym.myapp.domain.Acronym;
import com.acronym.myapp.domain.Context;
import com.acronym.myapp.domain.User;
import com.acronym.myapp.service.dto.AcronymDTO;
import com.acronym.myapp.service.dto.ContextDTO;
import com.acronym.myapp.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Acronym} and its DTO {@link AcronymDTO}.
 */
@Mapper(componentModel = "spring")
public interface AcronymMapper extends EntityMapper<AcronymDTO, Acronym> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "contexts", source = "contexts", qualifiedByName = "contextNameSet")
    AcronymDTO toDto(Acronym s);

    @Mapping(target = "removeContext", ignore = true)
    Acronym toEntity(AcronymDTO acronymDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("contextName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ContextDTO toDtoContextName(Context context);

    @Named("contextNameSet")
    default Set<ContextDTO> toDtoContextNameSet(Set<Context> context) {
        return context.stream().map(this::toDtoContextName).collect(Collectors.toSet());
    }
}
