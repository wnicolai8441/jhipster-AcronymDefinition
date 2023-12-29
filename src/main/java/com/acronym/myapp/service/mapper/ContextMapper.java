package com.acronym.myapp.service.mapper;

import com.acronym.myapp.domain.Context;
import com.acronym.myapp.service.dto.ContextDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Context} and its DTO {@link ContextDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContextMapper extends EntityMapper<ContextDTO, Context> {}
