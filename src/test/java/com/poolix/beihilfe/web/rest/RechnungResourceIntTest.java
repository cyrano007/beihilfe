package com.poolix.beihilfe.web.rest;

import com.poolix.beihilfe.BeihilfeApp;

import com.poolix.beihilfe.domain.Rechnung;
import com.poolix.beihilfe.repository.RechnungRepository;
import com.poolix.beihilfe.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.poolix.beihilfe.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RechnungResource REST controller.
 *
 * @see RechnungResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeihilfeApp.class)
public class RechnungResourceIntTest {

    private static final String DEFAULT_RECHNUNG_ID = "AAAAAAAAAA";
    private static final String UPDATED_RECHNUNG_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_AUSTELLUNGS_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_AUSTELLUNGS_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_BEZAHL_DATE = "AAAAAAAAAA";
    private static final String UPDATED_BEZAHL_DATE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BETRAG = new BigDecimal(1);
    private static final BigDecimal UPDATED_BETRAG = new BigDecimal(2);

    @Autowired
    private RechnungRepository rechnungRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRechnungMockMvc;

    private Rechnung rechnung;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RechnungResource rechnungResource = new RechnungResource(rechnungRepository);
        this.restRechnungMockMvc = MockMvcBuilders.standaloneSetup(rechnungResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rechnung createEntity(EntityManager em) {
        Rechnung rechnung = new Rechnung()
            .rechnungID(DEFAULT_RECHNUNG_ID)
            .austellungsDate(DEFAULT_AUSTELLUNGS_DATE)
            .bezahlDate(DEFAULT_BEZAHL_DATE)
            .betrag(DEFAULT_BETRAG);
        return rechnung;
    }

    @Before
    public void initTest() {
        rechnung = createEntity(em);
    }

    @Test
    @Transactional
    public void createRechnung() throws Exception {
        int databaseSizeBeforeCreate = rechnungRepository.findAll().size();

        // Create the Rechnung
        restRechnungMockMvc.perform(post("/api/rechnungs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rechnung)))
            .andExpect(status().isCreated());

        // Validate the Rechnung in the database
        List<Rechnung> rechnungList = rechnungRepository.findAll();
        assertThat(rechnungList).hasSize(databaseSizeBeforeCreate + 1);
        Rechnung testRechnung = rechnungList.get(rechnungList.size() - 1);
        assertThat(testRechnung.getRechnungID()).isEqualTo(DEFAULT_RECHNUNG_ID);
        assertThat(testRechnung.getAustellungsDate()).isEqualTo(DEFAULT_AUSTELLUNGS_DATE);
        assertThat(testRechnung.getBezahlDate()).isEqualTo(DEFAULT_BEZAHL_DATE);
        assertThat(testRechnung.getBetrag()).isEqualTo(DEFAULT_BETRAG);
    }

    @Test
    @Transactional
    public void createRechnungWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rechnungRepository.findAll().size();

        // Create the Rechnung with an existing ID
        rechnung.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRechnungMockMvc.perform(post("/api/rechnungs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rechnung)))
            .andExpect(status().isBadRequest());

        // Validate the Rechnung in the database
        List<Rechnung> rechnungList = rechnungRepository.findAll();
        assertThat(rechnungList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRechnungIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = rechnungRepository.findAll().size();
        // set the field null
        rechnung.setRechnungID(null);

        // Create the Rechnung, which fails.

        restRechnungMockMvc.perform(post("/api/rechnungs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rechnung)))
            .andExpect(status().isBadRequest());

        List<Rechnung> rechnungList = rechnungRepository.findAll();
        assertThat(rechnungList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAustellungsDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = rechnungRepository.findAll().size();
        // set the field null
        rechnung.setAustellungsDate(null);

        // Create the Rechnung, which fails.

        restRechnungMockMvc.perform(post("/api/rechnungs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rechnung)))
            .andExpect(status().isBadRequest());

        List<Rechnung> rechnungList = rechnungRepository.findAll();
        assertThat(rechnungList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBezahlDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = rechnungRepository.findAll().size();
        // set the field null
        rechnung.setBezahlDate(null);

        // Create the Rechnung, which fails.

        restRechnungMockMvc.perform(post("/api/rechnungs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rechnung)))
            .andExpect(status().isBadRequest());

        List<Rechnung> rechnungList = rechnungRepository.findAll();
        assertThat(rechnungList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRechnungs() throws Exception {
        // Initialize the database
        rechnungRepository.saveAndFlush(rechnung);

        // Get all the rechnungList
        restRechnungMockMvc.perform(get("/api/rechnungs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rechnung.getId().intValue())))
            .andExpect(jsonPath("$.[*].rechnungID").value(hasItem(DEFAULT_RECHNUNG_ID.toString())))
            .andExpect(jsonPath("$.[*].austellungsDate").value(hasItem(DEFAULT_AUSTELLUNGS_DATE.toString())))
            .andExpect(jsonPath("$.[*].bezahlDate").value(hasItem(DEFAULT_BEZAHL_DATE.toString())))
            .andExpect(jsonPath("$.[*].betrag").value(hasItem(DEFAULT_BETRAG.intValue())));
    }
    

    @Test
    @Transactional
    public void getRechnung() throws Exception {
        // Initialize the database
        rechnungRepository.saveAndFlush(rechnung);

        // Get the rechnung
        restRechnungMockMvc.perform(get("/api/rechnungs/{id}", rechnung.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rechnung.getId().intValue()))
            .andExpect(jsonPath("$.rechnungID").value(DEFAULT_RECHNUNG_ID.toString()))
            .andExpect(jsonPath("$.austellungsDate").value(DEFAULT_AUSTELLUNGS_DATE.toString()))
            .andExpect(jsonPath("$.bezahlDate").value(DEFAULT_BEZAHL_DATE.toString()))
            .andExpect(jsonPath("$.betrag").value(DEFAULT_BETRAG.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingRechnung() throws Exception {
        // Get the rechnung
        restRechnungMockMvc.perform(get("/api/rechnungs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRechnung() throws Exception {
        // Initialize the database
        rechnungRepository.saveAndFlush(rechnung);

        int databaseSizeBeforeUpdate = rechnungRepository.findAll().size();

        // Update the rechnung
        Rechnung updatedRechnung = rechnungRepository.findById(rechnung.getId()).get();
        // Disconnect from session so that the updates on updatedRechnung are not directly saved in db
        em.detach(updatedRechnung);
        updatedRechnung
            .rechnungID(UPDATED_RECHNUNG_ID)
            .austellungsDate(UPDATED_AUSTELLUNGS_DATE)
            .bezahlDate(UPDATED_BEZAHL_DATE)
            .betrag(UPDATED_BETRAG);

        restRechnungMockMvc.perform(put("/api/rechnungs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRechnung)))
            .andExpect(status().isOk());

        // Validate the Rechnung in the database
        List<Rechnung> rechnungList = rechnungRepository.findAll();
        assertThat(rechnungList).hasSize(databaseSizeBeforeUpdate);
        Rechnung testRechnung = rechnungList.get(rechnungList.size() - 1);
        assertThat(testRechnung.getRechnungID()).isEqualTo(UPDATED_RECHNUNG_ID);
        assertThat(testRechnung.getAustellungsDate()).isEqualTo(UPDATED_AUSTELLUNGS_DATE);
        assertThat(testRechnung.getBezahlDate()).isEqualTo(UPDATED_BEZAHL_DATE);
        assertThat(testRechnung.getBetrag()).isEqualTo(UPDATED_BETRAG);
    }

    @Test
    @Transactional
    public void updateNonExistingRechnung() throws Exception {
        int databaseSizeBeforeUpdate = rechnungRepository.findAll().size();

        // Create the Rechnung

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restRechnungMockMvc.perform(put("/api/rechnungs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rechnung)))
            .andExpect(status().isBadRequest());

        // Validate the Rechnung in the database
        List<Rechnung> rechnungList = rechnungRepository.findAll();
        assertThat(rechnungList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRechnung() throws Exception {
        // Initialize the database
        rechnungRepository.saveAndFlush(rechnung);

        int databaseSizeBeforeDelete = rechnungRepository.findAll().size();

        // Get the rechnung
        restRechnungMockMvc.perform(delete("/api/rechnungs/{id}", rechnung.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Rechnung> rechnungList = rechnungRepository.findAll();
        assertThat(rechnungList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rechnung.class);
        Rechnung rechnung1 = new Rechnung();
        rechnung1.setId(1L);
        Rechnung rechnung2 = new Rechnung();
        rechnung2.setId(rechnung1.getId());
        assertThat(rechnung1).isEqualTo(rechnung2);
        rechnung2.setId(2L);
        assertThat(rechnung1).isNotEqualTo(rechnung2);
        rechnung1.setId(null);
        assertThat(rechnung1).isNotEqualTo(rechnung2);
    }
}
