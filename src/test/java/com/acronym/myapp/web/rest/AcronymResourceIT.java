package com.acronym.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.acronym.myapp.IntegrationTest;
import com.acronym.myapp.domain.Acronym;
import com.acronym.myapp.repository.AcronymRepository;
import com.acronym.myapp.service.AcronymService;
import com.acronym.myapp.service.dto.AcronymDTO;
import com.acronym.myapp.service.mapper.AcronymMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AcronymResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AcronymResourceIT {

    private static final String DEFAULT_TERM_OR_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_TERM_OR_ACRONYM = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEFINITION = "AAAAAAAAAA";
    private static final String UPDATED_DEFINITION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/acronyms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcronymRepository acronymRepository;

    @Mock
    private AcronymRepository acronymRepositoryMock;

    @Autowired
    private AcronymMapper acronymMapper;

    @Mock
    private AcronymService acronymServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcronymMockMvc;

    private Acronym acronym;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acronym createEntity(EntityManager em) {
        Acronym acronym = new Acronym().termOrAcronym(DEFAULT_TERM_OR_ACRONYM).name(DEFAULT_NAME).definition(DEFAULT_DEFINITION);
        return acronym;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acronym createUpdatedEntity(EntityManager em) {
        Acronym acronym = new Acronym().termOrAcronym(UPDATED_TERM_OR_ACRONYM).name(UPDATED_NAME).definition(UPDATED_DEFINITION);
        return acronym;
    }

    @BeforeEach
    public void initTest() {
        acronym = createEntity(em);
    }

    @Test
    @Transactional
    void createAcronym() throws Exception {
        int databaseSizeBeforeCreate = acronymRepository.findAll().size();
        // Create the Acronym
        AcronymDTO acronymDTO = acronymMapper.toDto(acronym);
        restAcronymMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acronymDTO)))
            .andExpect(status().isCreated());

        // Validate the Acronym in the database
        List<Acronym> acronymList = acronymRepository.findAll();
        assertThat(acronymList).hasSize(databaseSizeBeforeCreate + 1);
        Acronym testAcronym = acronymList.get(acronymList.size() - 1);
        assertThat(testAcronym.getTermOrAcronym()).isEqualTo(DEFAULT_TERM_OR_ACRONYM);
        assertThat(testAcronym.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAcronym.getDefinition()).isEqualTo(DEFAULT_DEFINITION);
    }

    @Test
    @Transactional
    void createAcronymWithExistingId() throws Exception {
        // Create the Acronym with an existing ID
        acronym.setId(1L);
        AcronymDTO acronymDTO = acronymMapper.toDto(acronym);

        int databaseSizeBeforeCreate = acronymRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcronymMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acronymDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Acronym in the database
        List<Acronym> acronymList = acronymRepository.findAll();
        assertThat(acronymList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTermOrAcronymIsRequired() throws Exception {
        int databaseSizeBeforeTest = acronymRepository.findAll().size();
        // set the field null
        acronym.setTermOrAcronym(null);

        // Create the Acronym, which fails.
        AcronymDTO acronymDTO = acronymMapper.toDto(acronym);

        restAcronymMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acronymDTO)))
            .andExpect(status().isBadRequest());

        List<Acronym> acronymList = acronymRepository.findAll();
        assertThat(acronymList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAcronyms() throws Exception {
        // Initialize the database
        acronymRepository.saveAndFlush(acronym);

        // Get all the acronymList
        restAcronymMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acronym.getId().intValue())))
            .andExpect(jsonPath("$.[*].termOrAcronym").value(hasItem(DEFAULT_TERM_OR_ACRONYM)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].definition").value(hasItem(DEFAULT_DEFINITION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAcronymsWithEagerRelationshipsIsEnabled() throws Exception {
        when(acronymServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAcronymMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(acronymServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAcronymsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(acronymServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAcronymMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(acronymRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAcronym() throws Exception {
        // Initialize the database
        acronymRepository.saveAndFlush(acronym);

        // Get the acronym
        restAcronymMockMvc
            .perform(get(ENTITY_API_URL_ID, acronym.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(acronym.getId().intValue()))
            .andExpect(jsonPath("$.termOrAcronym").value(DEFAULT_TERM_OR_ACRONYM))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.definition").value(DEFAULT_DEFINITION));
    }

    @Test
    @Transactional
    void getNonExistingAcronym() throws Exception {
        // Get the acronym
        restAcronymMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAcronym() throws Exception {
        // Initialize the database
        acronymRepository.saveAndFlush(acronym);

        int databaseSizeBeforeUpdate = acronymRepository.findAll().size();

        // Update the acronym
        Acronym updatedAcronym = acronymRepository.findById(acronym.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAcronym are not directly saved in db
        em.detach(updatedAcronym);
        updatedAcronym.termOrAcronym(UPDATED_TERM_OR_ACRONYM).name(UPDATED_NAME).definition(UPDATED_DEFINITION);
        AcronymDTO acronymDTO = acronymMapper.toDto(updatedAcronym);

        restAcronymMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acronymDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acronymDTO))
            )
            .andExpect(status().isOk());

        // Validate the Acronym in the database
        List<Acronym> acronymList = acronymRepository.findAll();
        assertThat(acronymList).hasSize(databaseSizeBeforeUpdate);
        Acronym testAcronym = acronymList.get(acronymList.size() - 1);
        assertThat(testAcronym.getTermOrAcronym()).isEqualTo(UPDATED_TERM_OR_ACRONYM);
        assertThat(testAcronym.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAcronym.getDefinition()).isEqualTo(UPDATED_DEFINITION);
    }

    @Test
    @Transactional
    void putNonExistingAcronym() throws Exception {
        int databaseSizeBeforeUpdate = acronymRepository.findAll().size();
        acronym.setId(longCount.incrementAndGet());

        // Create the Acronym
        AcronymDTO acronymDTO = acronymMapper.toDto(acronym);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcronymMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acronymDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acronymDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acronym in the database
        List<Acronym> acronymList = acronymRepository.findAll();
        assertThat(acronymList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcronym() throws Exception {
        int databaseSizeBeforeUpdate = acronymRepository.findAll().size();
        acronym.setId(longCount.incrementAndGet());

        // Create the Acronym
        AcronymDTO acronymDTO = acronymMapper.toDto(acronym);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcronymMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acronymDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acronym in the database
        List<Acronym> acronymList = acronymRepository.findAll();
        assertThat(acronymList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcronym() throws Exception {
        int databaseSizeBeforeUpdate = acronymRepository.findAll().size();
        acronym.setId(longCount.incrementAndGet());

        // Create the Acronym
        AcronymDTO acronymDTO = acronymMapper.toDto(acronym);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcronymMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acronymDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Acronym in the database
        List<Acronym> acronymList = acronymRepository.findAll();
        assertThat(acronymList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAcronymWithPatch() throws Exception {
        // Initialize the database
        acronymRepository.saveAndFlush(acronym);

        int databaseSizeBeforeUpdate = acronymRepository.findAll().size();

        // Update the acronym using partial update
        Acronym partialUpdatedAcronym = new Acronym();
        partialUpdatedAcronym.setId(acronym.getId());

        partialUpdatedAcronym.termOrAcronym(UPDATED_TERM_OR_ACRONYM).name(UPDATED_NAME);

        restAcronymMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcronym.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcronym))
            )
            .andExpect(status().isOk());

        // Validate the Acronym in the database
        List<Acronym> acronymList = acronymRepository.findAll();
        assertThat(acronymList).hasSize(databaseSizeBeforeUpdate);
        Acronym testAcronym = acronymList.get(acronymList.size() - 1);
        assertThat(testAcronym.getTermOrAcronym()).isEqualTo(UPDATED_TERM_OR_ACRONYM);
        assertThat(testAcronym.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAcronym.getDefinition()).isEqualTo(DEFAULT_DEFINITION);
    }

    @Test
    @Transactional
    void fullUpdateAcronymWithPatch() throws Exception {
        // Initialize the database
        acronymRepository.saveAndFlush(acronym);

        int databaseSizeBeforeUpdate = acronymRepository.findAll().size();

        // Update the acronym using partial update
        Acronym partialUpdatedAcronym = new Acronym();
        partialUpdatedAcronym.setId(acronym.getId());

        partialUpdatedAcronym.termOrAcronym(UPDATED_TERM_OR_ACRONYM).name(UPDATED_NAME).definition(UPDATED_DEFINITION);

        restAcronymMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcronym.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcronym))
            )
            .andExpect(status().isOk());

        // Validate the Acronym in the database
        List<Acronym> acronymList = acronymRepository.findAll();
        assertThat(acronymList).hasSize(databaseSizeBeforeUpdate);
        Acronym testAcronym = acronymList.get(acronymList.size() - 1);
        assertThat(testAcronym.getTermOrAcronym()).isEqualTo(UPDATED_TERM_OR_ACRONYM);
        assertThat(testAcronym.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAcronym.getDefinition()).isEqualTo(UPDATED_DEFINITION);
    }

    @Test
    @Transactional
    void patchNonExistingAcronym() throws Exception {
        int databaseSizeBeforeUpdate = acronymRepository.findAll().size();
        acronym.setId(longCount.incrementAndGet());

        // Create the Acronym
        AcronymDTO acronymDTO = acronymMapper.toDto(acronym);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcronymMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, acronymDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acronymDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acronym in the database
        List<Acronym> acronymList = acronymRepository.findAll();
        assertThat(acronymList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcronym() throws Exception {
        int databaseSizeBeforeUpdate = acronymRepository.findAll().size();
        acronym.setId(longCount.incrementAndGet());

        // Create the Acronym
        AcronymDTO acronymDTO = acronymMapper.toDto(acronym);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcronymMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acronymDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acronym in the database
        List<Acronym> acronymList = acronymRepository.findAll();
        assertThat(acronymList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcronym() throws Exception {
        int databaseSizeBeforeUpdate = acronymRepository.findAll().size();
        acronym.setId(longCount.incrementAndGet());

        // Create the Acronym
        AcronymDTO acronymDTO = acronymMapper.toDto(acronym);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcronymMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(acronymDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Acronym in the database
        List<Acronym> acronymList = acronymRepository.findAll();
        assertThat(acronymList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAcronym() throws Exception {
        // Initialize the database
        acronymRepository.saveAndFlush(acronym);

        int databaseSizeBeforeDelete = acronymRepository.findAll().size();

        // Delete the acronym
        restAcronymMockMvc
            .perform(delete(ENTITY_API_URL_ID, acronym.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Acronym> acronymList = acronymRepository.findAll();
        assertThat(acronymList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
