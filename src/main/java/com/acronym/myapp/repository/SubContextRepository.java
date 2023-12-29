package com.acronym.myapp.repository;

import com.acronym.myapp.domain.SubContext;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubContext entity.
 */
@Repository
public interface SubContextRepository extends JpaRepository<SubContext, Long> {
    default Optional<SubContext> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SubContext> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SubContext> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select subContext from SubContext subContext left join fetch subContext.context",
        countQuery = "select count(subContext) from SubContext subContext"
    )
    Page<SubContext> findAllWithToOneRelationships(Pageable pageable);

    @Query("select subContext from SubContext subContext left join fetch subContext.context")
    List<SubContext> findAllWithToOneRelationships();

    @Query("select subContext from SubContext subContext left join fetch subContext.context where subContext.id =:id")
    Optional<SubContext> findOneWithToOneRelationships(@Param("id") Long id);
}
