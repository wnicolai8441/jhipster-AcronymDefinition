package com.acronym.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.acronym.myapp.IntegrationTest;
import com.acronym.myapp.domain.SubContext;
import com.acronym.myapp.repository.SubContextRepository;
import com.acronym.myapp.service.SubContextService;
import com.acronym.myapp.service.dto.SubContextDTO;
import com.acronym.myapp.service.mapper.SubContextMapper;
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
 * Integration tests for the {@link SubContextResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SubContextResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sub-contexts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubContextRepository subContextRepository;

    @Mock
    private SubContextRepository subContextRepositoryMock;

    @Autowired
    private SubContextMapper subContextMapper;

    @Mock
    private SubContextService subContextServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubContextMockMvc;

    private SubContext subContext;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubContext createEntity(EntityManager em) {
        SubContext subContext = new SubContext().name(DEFAULT_NAME);
        return subContext;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubContext createUpdatedEntity(EntityManager em) {
        SubContext subContext = new SubContext().name(UPDATED_NAME);
        return subContext;
    }

    @BeforeEach
    public void initTest() {
        subContext = createEntity(em);
    }

    @Test
    @Transactional
    void createSubContext() throws Exception {
        int databaseSizeBeforeCreate = subContextRepository.findAll().size();
        // Create the SubContext
        SubContextDTO subContextDTO = subContextMapper.toDto(subContext);
        restSubContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subContextDTO)))
            .andExpect(status().isCreated());

        // Validate the SubContext in the database
        List<SubContext> subContextList = subContextRepository.findAll();
        assertThat(subContextList).hasSize(databaseSizeBeforeCreate + 1);
        SubContext testSubContext = subContextList.get(subContextList.size() - 1);
        assertThat(testSubContext.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSubContextWithExistingId() throws Exception {
        // Create the SubContext with an existing ID
        subContext.setId(1L);
        SubContextDTO subContextDTO = subContextMapper.toDto(subContext);

        int databaseSizeBeforeCreate = subContextRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subContextDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubContext in the database
        List<SubContext> subContextList = subContextRepository.findAll();
        assertThat(subContextList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subContextRepository.findAll().size();
        // set the field null
        subContext.setName(null);

        // Create the SubContext, which fails.
        SubContextDTO subContextDTO = subContextMapper.toDto(subContext);

        restSubContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subContextDTO)))
            .andExpect(status().isBadRequest());

        List<SubContext> subContextList = subContextRepository.findAll();
        assertThat(subContextList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubContexts() throws Exception {
        // Initialize the database
        subContextRepository.saveAndFlush(subContext);

        // Get all the subContextList
        restSubContextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subContext.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubContextsWithEagerRelationshipsIsEnabled() throws Exception {
        when(subContextServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubContextMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(subContextServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubContextsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(subContextServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubContextMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(subContextRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSubContext() throws Exception {
        // Initialize the database
        subContextRepository.saveAndFlush(subContext);

        // Get the subContext
        restSubContextMockMvc
            .perform(get(ENTITY_API_URL_ID, subContext.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subContext.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSubContext() throws Exception {
        // Get the subContext
        restSubContextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubContext() throws Exception {
        // Initialize the database
        subContextRepository.saveAndFlush(subContext);

        int databaseSizeBeforeUpdate = subContextRepository.findAll().size();

        // Update the subContext
        SubContext updatedSubContext = subContextRepository.findById(subContext.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubContext are not directly saved in db
        em.detach(updatedSubContext);
        updatedSubContext.name(UPDATED_NAME);
        SubContextDTO subContextDTO = subContextMapper.toDto(updatedSubContext);

        restSubContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subContextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subContextDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubContext in the database
        List<SubContext> subContextList = subContextRepository.findAll();
        assertThat(subContextList).hasSize(databaseSizeBeforeUpdate);
        SubContext testSubContext = subContextList.get(subContextList.size() - 1);
        assertThat(testSubContext.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSubContext() throws Exception {
        int databaseSizeBeforeUpdate = subContextRepository.findAll().size();
        subContext.setId(longCount.incrementAndGet());

        // Create the SubContext
        SubContextDTO subContextDTO = subContextMapper.toDto(subContext);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subContextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subContextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubContext in the database
        List<SubContext> subContextList = subContextRepository.findAll();
        assertThat(subContextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubContext() throws Exception {
        int databaseSizeBeforeUpdate = subContextRepository.findAll().size();
        subContext.setId(longCount.incrementAndGet());

        // Create the SubContext
        SubContextDTO subContextDTO = subContextMapper.toDto(subContext);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subContextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubContext in the database
        List<SubContext> subContextList = subContextRepository.findAll();
        assertThat(subContextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubContext() throws Exception {
        int databaseSizeBeforeUpdate = subContextRepository.findAll().size();
        subContext.setId(longCount.incrementAndGet());

        // Create the SubContext
        SubContextDTO subContextDTO = subContextMapper.toDto(subContext);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubContextMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subContextDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubContext in the database
        List<SubContext> subContextList = subContextRepository.findAll();
        assertThat(subContextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubContextWithPatch() throws Exception {
        // Initialize the database
        subContextRepository.saveAndFlush(subContext);

        int databaseSizeBeforeUpdate = subContextRepository.findAll().size();

        // Update the subContext using partial update
        SubContext partialUpdatedSubContext = new SubContext();
        partialUpdatedSubContext.setId(subContext.getId());

        partialUpdatedSubContext.name(UPDATED_NAME);

        restSubContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubContext.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubContext))
            )
            .andExpect(status().isOk());

        // Validate the SubContext in the database
        List<SubContext> subContextList = subContextRepository.findAll();
        assertThat(subContextList).hasSize(databaseSizeBeforeUpdate);
        SubContext testSubContext = subContextList.get(subContextList.size() - 1);
        assertThat(testSubContext.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSubContextWithPatch() throws Exception {
        // Initialize the database
        subContextRepository.saveAndFlush(subContext);

        int databaseSizeBeforeUpdate = subContextRepository.findAll().size();

        // Update the subContext using partial update
        SubContext partialUpdatedSubContext = new SubContext();
        partialUpdatedSubContext.setId(subContext.getId());

        partialUpdatedSubContext.name(UPDATED_NAME);

        restSubContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubContext.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubContext))
            )
            .andExpect(status().isOk());

        // Validate the SubContext in the database
        List<SubContext> subContextList = subContextRepository.findAll();
        assertThat(subContextList).hasSize(databaseSizeBeforeUpdate);
        SubContext testSubContext = subContextList.get(subContextList.size() - 1);
        assertThat(testSubContext.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSubContext() throws Exception {
        int databaseSizeBeforeUpdate = subContextRepository.findAll().size();
        subContext.setId(longCount.incrementAndGet());

        // Create the SubContext
        SubContextDTO subContextDTO = subContextMapper.toDto(subContext);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subContextDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subContextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubContext in the database
        List<SubContext> subContextList = subContextRepository.findAll();
        assertThat(subContextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubContext() throws Exception {
        int databaseSizeBeforeUpdate = subContextRepository.findAll().size();
        subContext.setId(longCount.incrementAndGet());

        // Create the SubContext
        SubContextDTO subContextDTO = subContextMapper.toDto(subContext);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subContextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubContext in the database
        List<SubContext> subContextList = subContextRepository.findAll();
        assertThat(subContextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubContext() throws Exception {
        int databaseSizeBeforeUpdate = subContextRepository.findAll().size();
        subContext.setId(longCount.incrementAndGet());

        // Create the SubContext
        SubContextDTO subContextDTO = subContextMapper.toDto(subContext);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubContextMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(subContextDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubContext in the database
        List<SubContext> subContextList = subContextRepository.findAll();
        assertThat(subContextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubContext() throws Exception {
        // Initialize the database
        subContextRepository.saveAndFlush(subContext);

        int databaseSizeBeforeDelete = subContextRepository.findAll().size();

        // Delete the subContext
        restSubContextMockMvc
            .perform(delete(ENTITY_API_URL_ID, subContext.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubContext> subContextList = subContextRepository.findAll();
        assertThat(subContextList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
