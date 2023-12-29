package com.acronym.myapp.repository;

import com.acronym.myapp.domain.Context;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Context entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContextRepository extends JpaRepository<Context, Long> {}
