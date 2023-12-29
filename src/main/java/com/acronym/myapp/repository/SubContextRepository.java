package com.acronym.myapp.repository;

import com.acronym.myapp.domain.SubContext;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubContext entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubContextRepository extends JpaRepository<SubContext, Long> {}
