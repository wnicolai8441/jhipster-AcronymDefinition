package com.acronym.myapp.repository;

import com.acronym.myapp.domain.Acronym;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AcronymRepositoryWithBagRelationships {
    Optional<Acronym> fetchBagRelationships(Optional<Acronym> acronym);

    List<Acronym> fetchBagRelationships(List<Acronym> acronyms);

    Page<Acronym> fetchBagRelationships(Page<Acronym> acronyms);
}
