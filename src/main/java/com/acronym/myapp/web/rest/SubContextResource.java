package com.acronym.myapp.web.rest;

import com.acronym.myapp.repository.SubContextRepository;
import com.acronym.myapp.service.SubContextService;
import com.acronym.myapp.service.dto.SubContextDTO;
import com.acronym.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.acronym.myapp.domain.SubContext}.
 */
@RestController
@RequestMapping("/api/sub-contexts")
public class SubContextResource {

    private final Logger log = LoggerFactory.getLogger(SubContextResource.class);

    private static final String ENTITY_NAME = "subContext";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubContextService subContextService;

    private final SubContextRepository subContextRepository;

    public SubContextResource(SubContextService subContextService, SubContextRepository subContextRepository) {
        this.subContextService = subContextService;
        this.subContextRepository = subContextRepository;
    }

    /**
     * {@code POST  /sub-contexts} : Create a new subContext.
     *
     * @param subContextDTO the subContextDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subContextDTO, or with status {@code 400 (Bad Request)} if the subContext has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubContextDTO> createSubContext(@Valid @RequestBody SubContextDTO subContextDTO) throws URISyntaxException {
        log.debug("REST request to save SubContext : {}", subContextDTO);
        if (subContextDTO.getId() != null) {
            throw new BadRequestAlertException("A new subContext cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubContextDTO result = subContextService.save(subContextDTO);
        return ResponseEntity
            .created(new URI("/api/sub-contexts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sub-contexts/:id} : Updates an existing subContext.
     *
     * @param id the id of the subContextDTO to save.
     * @param subContextDTO the subContextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subContextDTO,
     * or with status {@code 400 (Bad Request)} if the subContextDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subContextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubContextDTO> updateSubContext(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubContextDTO subContextDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubContext : {}, {}", id, subContextDTO);
        if (subContextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subContextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subContextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubContextDTO result = subContextService.update(subContextDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subContextDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sub-contexts/:id} : Partial updates given fields of an existing subContext, field will ignore if it is null
     *
     * @param id the id of the subContextDTO to save.
     * @param subContextDTO the subContextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subContextDTO,
     * or with status {@code 400 (Bad Request)} if the subContextDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subContextDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subContextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubContextDTO> partialUpdateSubContext(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubContextDTO subContextDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubContext partially : {}, {}", id, subContextDTO);
        if (subContextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subContextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subContextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubContextDTO> result = subContextService.partialUpdate(subContextDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subContextDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-contexts} : get all the subContexts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subContexts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubContextDTO>> getAllSubContexts(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of SubContexts");
        Page<SubContextDTO> page;
        if (eagerload) {
            page = subContextService.findAllWithEagerRelationships(pageable);
        } else {
            page = subContextService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sub-contexts/:id} : get the "id" subContext.
     *
     * @param id the id of the subContextDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subContextDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubContextDTO> getSubContext(@PathVariable("id") Long id) {
        log.debug("REST request to get SubContext : {}", id);
        Optional<SubContextDTO> subContextDTO = subContextService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subContextDTO);
    }

    /**
     * {@code DELETE  /sub-contexts/:id} : delete the "id" subContext.
     *
     * @param id the id of the subContextDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubContext(@PathVariable("id") Long id) {
        log.debug("REST request to delete SubContext : {}", id);
        subContextService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
