package com.acronym.myapp.service.mapper;

import com.acronym.myapp.domain.Context;
import com.acronym.myapp.domain.SubContext;
import com.acronym.myapp.service.dto.ContextDTO;
import com.acronym.myapp.service.dto.SubContextDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubContext} and its DTO {@link SubContextDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubContextMapper extends EntityMapper<SubContextDTO, SubContext> {
    @Mapping(target = "contexts", source = "contexts", qualifiedByName = "contextNameSet")
    SubContextDTO toDto(SubContext s);

    @Mapping(target = "removeContext", ignore = true)
    SubContext toEntity(SubContextDTO subContextDTO);

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
