package com.acronym.myapp.service;

import com.acronym.myapp.service.dto.AcronymDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.acronym.myapp.domain.Acronym}.
 */
public interface AcronymService {
    /**
     * Save a acronym.
     *
     * @param acronymDTO the entity to save.
     * @return the persisted entity.
     */
    AcronymDTO save(AcronymDTO acronymDTO);

    /**
     * Updates a acronym.
     *
     * @param acronymDTO the entity to update.
     * @return the persisted entity.
     */
    AcronymDTO update(AcronymDTO acronymDTO);

    /**
     * Partially updates a acronym.
     *
     * @param acronymDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AcronymDTO> partialUpdate(AcronymDTO acronymDTO);

    /**
     * Get all the acronyms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AcronymDTO> findAll(Pageable pageable);

    /**
     * Get all the acronyms with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AcronymDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" acronym.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AcronymDTO> findOne(Long id);

    /**
     * Delete the "id" acronym.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
