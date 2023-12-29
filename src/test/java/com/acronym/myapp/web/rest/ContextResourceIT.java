package com.acronym.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.acronym.myapp.IntegrationTest;
import com.acronym.myapp.domain.Context;
import com.acronym.myapp.repository.ContextRepository;
import com.acronym.myapp.service.dto.ContextDTO;
import com.acronym.myapp.service.mapper.ContextMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContextResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContextResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contexts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContextRepository contextRepository;

    @Autowired
    private ContextMapper contextMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContextMockMvc;

    private Context context;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Context createEntity(EntityManager em) {
        Context context = new Context().name(DEFAULT_NAME);
        return context;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Context createUpdatedEntity(EntityManager em) {
        Context context = new Context().name(UPDATED_NAME);
        return context;
    }

    @BeforeEach
    public void initTest() {
        context = createEntity(em);
    }

    @Test
    @Transactional
    void createContext() throws Exception {
        int databaseSizeBeforeCreate = contextRepository.findAll().size();
        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);
        restContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contextDTO)))
            .andExpect(status().isCreated());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeCreate + 1);
        Context testContext = contextList.get(contextList.size() - 1);
        assertThat(testContext.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createContextWithExistingId() throws Exception {
        // Create the Context with an existing ID
        context.setId(1L);
        ContextDTO contextDTO = contextMapper.toDto(context);

        int databaseSizeBeforeCreate = contextRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contextDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contextRepository.findAll().size();
        // set the field null
        context.setName(null);

        // Create the Context, which fails.
        ContextDTO contextDTO = contextMapper.toDto(context);

        restContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contextDTO)))
            .andExpect(status().isBadRequest());

        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContexts() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        // Get all the contextList
        restContextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(context.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        // Get the context
        restContextMockMvc
            .perform(get(ENTITY_API_URL_ID, context.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(context.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingContext() throws Exception {
        // Get the context
        restContextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        int databaseSizeBeforeUpdate = contextRepository.findAll().size();

        // Update the context
        Context updatedContext = contextRepository.findById(context.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContext are not directly saved in db
        em.detach(updatedContext);
        updatedContext.name(UPDATED_NAME);
        ContextDTO contextDTO = contextMapper.toDto(updatedContext);

        restContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contextDTO))
            )
            .andExpect(status().isOk());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
        Context testContext = contextList.get(contextList.size() - 1);
        assertThat(testContext.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();
        context.setId(longCount.incrementAndGet());

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();
        context.setId(longCount.incrementAndGet());

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();
        context.setId(longCount.incrementAndGet());

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contextDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContextWithPatch() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        int databaseSizeBeforeUpdate = contextRepository.findAll().size();

        // Update the context using partial update
        Context partialUpdatedContext = new Context();
        partialUpdatedContext.setId(context.getId());

        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContext.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContext))
            )
            .andExpect(status().isOk());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
        Context testContext = contextList.get(contextList.size() - 1);
        assertThat(testContext.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateContextWithPatch() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        int databaseSizeBeforeUpdate = contextRepository.findAll().size();

        // Update the context using partial update
        Context partialUpdatedContext = new Context();
        partialUpdatedContext.setId(context.getId());

        partialUpdatedContext.name(UPDATED_NAME);

        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContext.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContext))
            )
            .andExpect(status().isOk());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
        Context testContext = contextList.get(contextList.size() - 1);
        assertThat(testContext.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();
        context.setId(longCount.incrementAndGet());

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contextDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();
        context.setId(longCount.incrementAndGet());

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();
        context.setId(longCount.incrementAndGet());

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contextDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        int databaseSizeBeforeDelete = contextRepository.findAll().size();

        // Delete the context
        restContextMockMvc
            .perform(delete(ENTITY_API_URL_ID, context.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
