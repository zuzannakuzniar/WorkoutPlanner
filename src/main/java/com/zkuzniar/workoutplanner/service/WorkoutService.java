package com.zkuzniar.workoutplanner.service;

import com.zkuzniar.workoutplanner.service.dto.WorkoutDTO;
import java.util.List;
import java.util.Optional;
public interface WorkoutService {

    WorkoutDTO save(WorkoutDTO workoutDTO);

    Optional<WorkoutDTO> partialUpdate(WorkoutDTO workoutDTO);

    List<WorkoutDTO> findAll();

    Optional<WorkoutDTO> findOne(Long id);

    void delete(Long id);
}
