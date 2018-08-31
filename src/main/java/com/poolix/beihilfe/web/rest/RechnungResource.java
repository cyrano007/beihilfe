package com.poolix.beihilfe.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.poolix.beihilfe.domain.Rechnung;
import com.poolix.beihilfe.repository.RechnungRepository;
import com.poolix.beihilfe.web.rest.errors.BadRequestAlertException;
import com.poolix.beihilfe.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Rechnung.
 */
@RestController
@RequestMapping("/api")
public class RechnungResource {

    private final Logger log = LoggerFactory.getLogger(RechnungResource.class);

    private static final String ENTITY_NAME = "rechnung";

    private final RechnungRepository rechnungRepository;

    public RechnungResource(RechnungRepository rechnungRepository) {
        this.rechnungRepository = rechnungRepository;
    }

    /**
     * POST  /rechnungs : Create a new rechnung.
     *
     * @param rechnung the rechnung to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rechnung, or with status 400 (Bad Request) if the rechnung has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rechnungs")
    @Timed
    public ResponseEntity<Rechnung> createRechnung(@Valid @RequestBody Rechnung rechnung) throws URISyntaxException {
        log.debug("REST request to save Rechnung : {}", rechnung);
        if (rechnung.getId() != null) {
            throw new BadRequestAlertException("A new rechnung cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rechnung result = rechnungRepository.save(rechnung);
        return ResponseEntity.created(new URI("/api/rechnungs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rechnungs : Updates an existing rechnung.
     *
     * @param rechnung the rechnung to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rechnung,
     * or with status 400 (Bad Request) if the rechnung is not valid,
     * or with status 500 (Internal Server Error) if the rechnung couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rechnungs")
    @Timed
    public ResponseEntity<Rechnung> updateRechnung(@Valid @RequestBody Rechnung rechnung) throws URISyntaxException {
        log.debug("REST request to update Rechnung : {}", rechnung);
        if (rechnung.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Rechnung result = rechnungRepository.save(rechnung);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rechnung.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rechnungs : get all the rechnungs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rechnungs in body
     */
    @GetMapping("/rechnungs")
    @Timed
    public List<Rechnung> getAllRechnungs() {
        log.debug("REST request to get all Rechnungs");
        return rechnungRepository.findAll();
    }

    /**
     * GET  /rechnungs/:id : get the "id" rechnung.
     *
     * @param id the id of the rechnung to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rechnung, or with status 404 (Not Found)
     */
    @GetMapping("/rechnungs/{id}")
    @Timed
    public ResponseEntity<Rechnung> getRechnung(@PathVariable Long id) {
        log.debug("REST request to get Rechnung : {}", id);
        Optional<Rechnung> rechnung = rechnungRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rechnung);
    }

    /**
     * DELETE  /rechnungs/:id : delete the "id" rechnung.
     *
     * @param id the id of the rechnung to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rechnungs/{id}")
    @Timed
    public ResponseEntity<Void> deleteRechnung(@PathVariable Long id) {
        log.debug("REST request to delete Rechnung : {}", id);

        rechnungRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
