package com.acronym.myapp.web.rest;

import com.acronym.myapp.repository.AcronymRepository;
import com.acronym.myapp.service.AcronymService;
import com.acronym.myapp.service.dto.AcronymDTO;
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
 * REST controller for managing {@link com.acronym.myapp.domain.Acronym}.
 */
@RestController
@RequestMapping("/api/acronyms")
public class AcronymResource {

    private final Logger log = LoggerFactory.getLogger(AcronymResource.class);

    private static final String ENTITY_NAME = "acronym";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcronymService acronymService;

    private final AcronymRepository acronymRepository;

    public AcronymResource(AcronymService acronymService, AcronymRepository acronymRepository) {
        this.acronymService = acronymService;
        this.acronymRepository = acronymRepository;
    }

    /**
     * {@code POST  /acronyms} : Create a new acronym.
     *
     * @param acronymDTO the acronymDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acronymDTO, or with status {@code 400 (Bad Request)} if the acronym has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AcronymDTO> createAcronym(@Valid @RequestBody AcronymDTO acronymDTO) throws URISyntaxException {
        log.debug("REST request to save Acronym : {}", acronymDTO);
        if (acronymDTO.getId() != null) {
            throw new BadRequestAlertException("A new acronym cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AcronymDTO result = acronymService.save(acronymDTO);
        return ResponseEntity
            .created(new URI("/api/acronyms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acronyms/:id} : Updates an existing acronym.
     *
     * @param id the id of the acronymDTO to save.
     * @param acronymDTO the acronymDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acronymDTO,
     * or with status {@code 400 (Bad Request)} if the acronymDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acronymDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AcronymDTO> updateAcronym(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AcronymDTO acronymDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Acronym : {}, {}", id, acronymDTO);
        if (acronymDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acronymDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acronymRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AcronymDTO result = acronymService.update(acronymDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acronymDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acronyms/:id} : Partial updates given fields of an existing acronym, field will ignore if it is null
     *
     * @param id the id of the acronymDTO to save.
     * @param acronymDTO the acronymDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acronymDTO,
     * or with status {@code 400 (Bad Request)} if the acronymDTO is not valid,
     * or with status {@code 404 (Not Found)} if the acronymDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the acronymDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AcronymDTO> partialUpdateAcronym(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AcronymDTO acronymDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Acronym partially : {}, {}", id, acronymDTO);
        if (acronymDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acronymDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acronymRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AcronymDTO> result = acronymService.partialUpdate(acronymDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acronymDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /acronyms} : get all the acronyms.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acronyms in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AcronymDTO>> getAllAcronyms(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Acronyms");
        Page<AcronymDTO> page;
        if (eagerload) {
            page = acronymService.findAllWithEagerRelationships(pageable);
        } else {
            page = acronymService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acronyms/:id} : get the "id" acronym.
     *
     * @param id the id of the acronymDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acronymDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AcronymDTO> getAcronym(@PathVariable("id") Long id) {
        log.debug("REST request to get Acronym : {}", id);
        Optional<AcronymDTO> acronymDTO = acronymService.findOne(id);
        return ResponseUtil.wrapOrNotFound(acronymDTO);
    }

    /**
     * {@code DELETE  /acronyms/:id} : delete the "id" acronym.
     *
     * @param id the id of the acronymDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcronym(@PathVariable("id") Long id) {
        log.debug("REST request to delete Acronym : {}", id);
        acronymService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
