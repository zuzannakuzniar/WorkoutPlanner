package com.zkuzniar.workoutplanner.web.rest;

import com.zkuzniar.workoutplanner.repository.DayRepository;
import com.zkuzniar.workoutplanner.service.DayService;
import com.zkuzniar.workoutplanner.service.dto.DayDTO;
import com.zkuzniar.workoutplanner.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class DayResource {

    private final Logger log = LoggerFactory.getLogger(DayResource.class);

    private static final String ENTITY_NAME = "workoutPlannerDay";

    @Value("${workoutPlanner.clientApp.name}")
    private String applicationName;

    private final DayService dayService;

    private final DayRepository dayRepository;

    public DayResource(DayService dayService, DayRepository dayRepository) {
        this.dayService = dayService;
        this.dayRepository = dayRepository;
    }

    @PostMapping("/days")
    public ResponseEntity<DayDTO> createDay(@RequestBody DayDTO dayDTO) throws URISyntaxException {
        log.debug("REST request to save Day : {}", dayDTO);
        if (dayDTO.getId() != null) {
            throw new BadRequestAlertException("A new day cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DayDTO result = dayService.save(dayDTO);
        return ResponseEntity
            .created(new URI("/api/days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/days/{id}")
    public ResponseEntity<DayDTO> updateDay(@PathVariable(value = "id", required = false) final Long id, @RequestBody DayDTO dayDTO)
        throws URISyntaxException {
        log.debug("REST request to update Day : {}, {}", id, dayDTO);
        if (dayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DayDTO result = dayService.save(dayDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dayDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/days/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DayDTO> partialUpdateDay(@PathVariable(value = "id", required = false) final Long id, @RequestBody DayDTO dayDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update Day partially : {}, {}", id, dayDTO);
        if (dayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DayDTO> result = dayService.partialUpdate(dayDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dayDTO.getId().toString())
        );
    }

    @GetMapping("/days")
    public List<DayDTO> getAllDays() {
        log.debug("REST request to get all Days");
        return dayService.findAll();
    }

    @GetMapping("/days/{id}")
    public ResponseEntity<DayDTO> getDay(@PathVariable Long id) {
        log.debug("REST request to get Day : {}", id);
        Optional<DayDTO> dayDTO = dayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dayDTO);
    }

    @DeleteMapping("/days/{id}")
    public ResponseEntity<Void> deleteDay(@PathVariable Long id) {
        log.debug("REST request to delete Day : {}", id);
        dayService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
