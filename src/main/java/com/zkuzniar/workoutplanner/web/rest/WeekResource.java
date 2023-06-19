package com.zkuzniar.workoutplanner.web.rest;

import com.zkuzniar.workoutplanner.repository.WeekRepository;
import com.zkuzniar.workoutplanner.service.WeekService;
import com.zkuzniar.workoutplanner.service.dto.WeekDTO;
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
public class WeekResource {

    private final Logger log = LoggerFactory.getLogger(WeekResource.class);

    private static final String ENTITY_NAME = "workoutPlannerWeek";

    @Value("${workoutPlanner.clientApp.name}")
    private String applicationName;

    private final WeekService weekService;

    private final WeekRepository weekRepository;

    public WeekResource(WeekService weekService, WeekRepository weekRepository) {
        this.weekService = weekService;
        this.weekRepository = weekRepository;
    }

    @PostMapping("/weeks")
    public ResponseEntity<WeekDTO> createWeek(@RequestBody WeekDTO weekDTO) throws URISyntaxException {
        log.debug("REST request to save Week : {}", weekDTO);
        if (weekDTO.getId() != null) {
            throw new BadRequestAlertException("A new week cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeekDTO result = weekService.save(weekDTO);
        return ResponseEntity
            .created(new URI("/api/weeks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/weeks/{id}")
    public ResponseEntity<WeekDTO> updateWeek(@PathVariable(value = "id", required = false) final Long id, @RequestBody WeekDTO weekDTO)
        throws URISyntaxException {
        log.debug("REST request to update Week : {}, {}", id, weekDTO);
        if (weekDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weekDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weekRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WeekDTO result = weekService.save(weekDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, weekDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/weeks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WeekDTO> partialUpdateWeek(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WeekDTO weekDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Week partially : {}, {}", id, weekDTO);
        if (weekDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weekDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weekRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WeekDTO> result = weekService.partialUpdate(weekDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, weekDTO.getId().toString())
        );
    }

    @GetMapping("/weeks")
    public List<WeekDTO> getAllWeeks() {
        log.debug("REST request to get all Weeks");
        return weekService.findAll();
    }

    @GetMapping("/weeks/{id}")
    public ResponseEntity<WeekDTO> getWeek(@PathVariable Long id) {
        log.debug("REST request to get Week : {}", id);
        Optional<WeekDTO> weekDTO = weekService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weekDTO);
    }

    @DeleteMapping("/weeks/{id}")
    public ResponseEntity<Void> deleteWeek(@PathVariable Long id) {
        log.debug("REST request to delete Week : {}", id);
        weekService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
