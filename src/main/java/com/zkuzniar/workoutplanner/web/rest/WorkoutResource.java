package com.zkuzniar.workoutplanner.web.rest;

import com.zkuzniar.workoutplanner.repository.WorkoutRepository;
import com.zkuzniar.workoutplanner.service.WorkoutService;
import com.zkuzniar.workoutplanner.service.dto.WorkoutDTO;
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
public class WorkoutResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutResource.class);

    private static final String ENTITY_NAME = "workoutPlannerWorkout";

    @Value("${workoutPlanner.clientApp.name}")
    private String applicationName;

    private final WorkoutService workoutService;

    private final WorkoutRepository workoutRepository;

    public WorkoutResource(WorkoutService workoutService, WorkoutRepository workoutRepository) {
        this.workoutService = workoutService;
        this.workoutRepository = workoutRepository;
    }

    @PostMapping("/workouts")
    public ResponseEntity<WorkoutDTO> createWorkout(@RequestBody WorkoutDTO workoutDTO) throws URISyntaxException {
        log.debug("REST request to save Workout : {}", workoutDTO);
        if (workoutDTO.getId() != null) {
            throw new BadRequestAlertException("A new workout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkoutDTO result = workoutService.save(workoutDTO);
        return ResponseEntity
            .created(new URI("/api/workouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false,
                ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/workouts/{id}")
    public ResponseEntity<WorkoutDTO> updateWorkout(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutDTO workoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Workout : {}, {}", id, workoutDTO);
        if (workoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkoutDTO result = workoutService.save(workoutDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workoutDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/workouts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkoutDTO> partialUpdateWorkout(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkoutDTO workoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Workout partially : {}, {}", id, workoutDTO);
        if (workoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkoutDTO> result = workoutService.partialUpdate(workoutDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workoutDTO.getId().toString())
        );
    }

    @GetMapping("/workouts")
    public List<WorkoutDTO> getAllWorkouts() {
        log.debug("REST request to get all Workouts");
        return workoutService.findAll();
    }

    @GetMapping("/workouts/{id}")
    public ResponseEntity<WorkoutDTO> getWorkout(@PathVariable Long id) {
        log.debug("REST request to get Workout : {}", id);
        Optional<WorkoutDTO> workoutDTO = workoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutDTO);
    }

    @DeleteMapping("/workouts/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        log.debug("REST request to delete Workout : {}", id);
        workoutService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
