package com.acronym.myapp.service.mapper;

import com.acronym.myapp.domain.Context;
import com.acronym.myapp.domain.SubContext;
import com.acronym.myapp.service.dto.ContextDTO;
import com.acronym.myapp.service.dto.SubContextDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubContext} and its DTO {@link SubContextDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubContextMapper extends EntityMapper<SubContextDTO, SubContext> {
    @Mapping(target = "context", source = "context", qualifiedByName = "contextName")
    SubContextDTO toDto(SubContext s);

    @Named("contextName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ContextDTO toDtoContextName(Context context);
}
