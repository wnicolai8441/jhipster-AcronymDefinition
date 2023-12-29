package com.acronym.myapp.repository;

import com.acronym.myapp.domain.Acronym;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Acronym entity.
 *
 * When extending this class, extend AcronymRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AcronymRepository extends AcronymRepositoryWithBagRelationships, JpaRepository<Acronym, Long> {
    @Query("select acronym from Acronym acronym where acronym.user.login = ?#{authentication.name}")
    List<Acronym> findByUserIsCurrentUser();

    default Optional<Acronym> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Acronym> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Acronym> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select acronym from Acronym acronym left join fetch acronym.subContext left join fetch acronym.user",
        countQuery = "select count(acronym) from Acronym acronym"
    )
    Page<Acronym> findAllWithToOneRelationships(Pageable pageable);

    @Query("select acronym from Acronym acronym left join fetch acronym.subContext left join fetch acronym.user")
    List<Acronym> findAllWithToOneRelationships();

    @Query("select acronym from Acronym acronym left join fetch acronym.subContext left join fetch acronym.user where acronym.id =:id")
    Optional<Acronym> findOneWithToOneRelationships(@Param("id") Long id);
}
