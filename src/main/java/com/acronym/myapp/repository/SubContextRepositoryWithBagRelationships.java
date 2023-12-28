package com.acronym.myapp.repository;

import com.acronym.myapp.domain.SubContext;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SubContextRepositoryWithBagRelationships {
    Optional<SubContext> fetchBagRelationships(Optional<SubContext> subContext);

    List<SubContext> fetchBagRelationships(List<SubContext> subContexts);

    Page<SubContext> fetchBagRelationships(Page<SubContext> subContexts);
}
