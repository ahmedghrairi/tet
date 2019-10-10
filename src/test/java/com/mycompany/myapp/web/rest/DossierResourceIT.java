package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TetApp;
import com.mycompany.myapp.domain.Dossier;
import com.mycompany.myapp.repository.DossierRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DossierResource} REST controller.
 */
@SpringBootTest(classes = TetApp.class)
public class DossierResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private DossierRepository dossierRepository;

    @Mock
    private DossierRepository dossierRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDossierMockMvc;

    private Dossier dossier;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DossierResource dossierResource = new DossierResource(dossierRepository);
        this.restDossierMockMvc = MockMvcBuilders.standaloneSetup(dossierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dossier createEntity(EntityManager em) {
        Dossier dossier = new Dossier()
            .nom(DEFAULT_NOM);
        return dossier;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dossier createUpdatedEntity(EntityManager em) {
        Dossier dossier = new Dossier()
            .nom(UPDATED_NOM);
        return dossier;
    }

    @BeforeEach
    public void initTest() {
        dossier = createEntity(em);
    }

    @Test
    @Transactional
    public void createDossier() throws Exception {
        int databaseSizeBeforeCreate = dossierRepository.findAll().size();

        // Create the Dossier
        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isCreated());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate + 1);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    public void createDossierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dossierRepository.findAll().size();

        // Create the Dossier with an existing ID
        dossier.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDossiers() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList
        restDossierMockMvc.perform(get("/api/dossiers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossier.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllDossiersWithEagerRelationshipsIsEnabled() throws Exception {
        DossierResource dossierResource = new DossierResource(dossierRepositoryMock);
        when(dossierRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restDossierMockMvc = MockMvcBuilders.standaloneSetup(dossierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDossierMockMvc.perform(get("/api/dossiers?eagerload=true"))
        .andExpect(status().isOk());

        verify(dossierRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDossiersWithEagerRelationshipsIsNotEnabled() throws Exception {
        DossierResource dossierResource = new DossierResource(dossierRepositoryMock);
            when(dossierRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restDossierMockMvc = MockMvcBuilders.standaloneSetup(dossierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDossierMockMvc.perform(get("/api/dossiers?eagerload=true"))
        .andExpect(status().isOk());

            verify(dossierRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getDossier() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get the dossier
        restDossierMockMvc.perform(get("/api/dossiers/{id}", dossier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dossier.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDossier() throws Exception {
        // Get the dossier
        restDossierMockMvc.perform(get("/api/dossiers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDossier() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Update the dossier
        Dossier updatedDossier = dossierRepository.findById(dossier.getId()).get();
        // Disconnect from session so that the updates on updatedDossier are not directly saved in db
        em.detach(updatedDossier);
        updatedDossier
            .nom(UPDATED_NOM);

        restDossierMockMvc.perform(put("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDossier)))
            .andExpect(status().isOk());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    public void updateNonExistingDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Create the Dossier

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierMockMvc.perform(put("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDossier() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        int databaseSizeBeforeDelete = dossierRepository.findAll().size();

        // Delete the dossier
        restDossierMockMvc.perform(delete("/api/dossiers/{id}", dossier.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dossier.class);
        Dossier dossier1 = new Dossier();
        dossier1.setId(1L);
        Dossier dossier2 = new Dossier();
        dossier2.setId(dossier1.getId());
        assertThat(dossier1).isEqualTo(dossier2);
        dossier2.setId(2L);
        assertThat(dossier1).isNotEqualTo(dossier2);
        dossier1.setId(null);
        assertThat(dossier1).isNotEqualTo(dossier2);
    }
}
