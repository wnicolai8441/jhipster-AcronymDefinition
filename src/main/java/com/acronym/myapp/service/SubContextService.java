package com.acronym.myapp.service;

import com.acronym.myapp.service.dto.SubContextDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.acronym.myapp.domain.SubContext}.
 */
public interface SubContextService {
    /**
     * Save a subContext.
     *
     * @param subContextDTO the entity to save.
     * @return the persisted entity.
     */
    SubContextDTO save(SubContextDTO subContextDTO);

    /**
     * Updates a subContext.
     *
     * @param subContextDTO the entity to update.
     * @return the persisted entity.
     */
    SubContextDTO update(SubContextDTO subContextDTO);

    /**
     * Partially updates a subContext.
     *
     * @param subContextDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubContextDTO> partialUpdate(SubContextDTO subContextDTO);

    /**
     * Get all the subContexts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubContextDTO> findAll(Pageable pageable);

    /**
     * Get all the subContexts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubContextDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" subContext.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubContextDTO> findOne(Long id);

    /**
     * Delete the "id" subContext.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
