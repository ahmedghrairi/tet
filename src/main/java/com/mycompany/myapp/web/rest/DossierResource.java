package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Dossier;
import com.mycompany.myapp.repository.DossierRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Dossier}.
 */
@RestController
@RequestMapping("/api")
public class DossierResource {

    private final Logger log = LoggerFactory.getLogger(DossierResource.class);

    private static final String ENTITY_NAME = "dossier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DossierRepository dossierRepository;

    public DossierResource(DossierRepository dossierRepository) {
        this.dossierRepository = dossierRepository;
    }

    /**
     * {@code POST  /dossiers} : Create a new dossier.
     *
     * @param dossier the dossier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dossier, or with status {@code 400 (Bad Request)} if the dossier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dossiers")
    public ResponseEntity<Dossier> createDossier(@RequestBody Dossier dossier) throws URISyntaxException {
        log.debug("REST request to save Dossier : {}", dossier);
        if (dossier.getId() != null) {
            throw new BadRequestAlertException("A new dossier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dossier result = dossierRepository.save(dossier);
        return ResponseEntity.created(new URI("/api/dossiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dossiers} : Updates an existing dossier.
     *
     * @param dossier the dossier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossier,
     * or with status {@code 400 (Bad Request)} if the dossier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dossier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dossiers")
    public ResponseEntity<Dossier> updateDossier(@RequestBody Dossier dossier) throws URISyntaxException {
        log.debug("REST request to update Dossier : {}", dossier);
        if (dossier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Dossier result = dossierRepository.save(dossier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dossier.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dossiers} : get all the dossiers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dossiers in body.
     */
    @GetMapping("/dossiers")
    public List<Dossier> getAllDossiers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Dossiers");
        return dossierRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /dossiers/:id} : get the "id" dossier.
     *
     * @param id the id of the dossier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dossier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dossiers/{id}")
    public ResponseEntity<Dossier> getDossier(@PathVariable Long id) {
        log.debug("REST request to get Dossier : {}", id);
        Optional<Dossier> dossier = dossierRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(dossier);
    }

    /**
     * {@code DELETE  /dossiers/:id} : delete the "id" dossier.
     *
     * @param id the id of the dossier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dossiers/{id}")
    public ResponseEntity<Void> deleteDossier(@PathVariable Long id) {
        log.debug("REST request to delete Dossier : {}", id);
        dossierRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
