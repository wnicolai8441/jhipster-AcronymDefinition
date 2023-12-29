package com.acronym.myapp.service.mapper;

import com.acronym.myapp.domain.SubContext;
import com.acronym.myapp.service.dto.SubContextDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubContext} and its DTO {@link SubContextDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubContextMapper extends EntityMapper<SubContextDTO, SubContext> {}
