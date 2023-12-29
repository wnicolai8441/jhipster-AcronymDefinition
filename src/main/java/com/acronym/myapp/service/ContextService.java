package com.acronym.myapp.service;

import com.acronym.myapp.service.dto.ContextDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.acronym.myapp.domain.Context}.
 */
public interface ContextService {
    /**
     * Save a context.
     *
     * @param contextDTO the entity to save.
     * @return the persisted entity.
     */
    ContextDTO save(ContextDTO contextDTO);

    /**
     * Updates a context.
     *
     * @param contextDTO the entity to update.
     * @return the persisted entity.
     */
    ContextDTO update(ContextDTO contextDTO);

    /**
     * Partially updates a context.
     *
     * @param contextDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContextDTO> partialUpdate(ContextDTO contextDTO);

    /**
     * Get all the contexts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContextDTO> findAll(Pageable pageable);

    /**
     * Get the "id" context.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContextDTO> findOne(Long id);

    /**
     * Delete the "id" context.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
