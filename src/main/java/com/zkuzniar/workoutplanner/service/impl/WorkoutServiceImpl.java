package com.zkuzniar.workoutplanner.service.impl;

import com.zkuzniar.workoutplanner.domain.Workout;
import com.zkuzniar.workoutplanner.repository.WorkoutRepository;
import com.zkuzniar.workoutplanner.service.WorkoutService;
import com.zkuzniar.workoutplanner.service.dto.WorkoutDTO;
import com.zkuzniar.workoutplanner.service.mapper.WorkoutMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WorkoutServiceImpl implements WorkoutService {

    private final Logger log = LoggerFactory.getLogger(WorkoutServiceImpl.class);

    private final WorkoutRepository workoutRepository;

    private final WorkoutMapper workoutMapper;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository, WorkoutMapper workoutMapper) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
    }

    @Override
    public WorkoutDTO save(WorkoutDTO workoutDTO) {
        log.debug("Request to save Workout : {}", workoutDTO);
        Workout workout = workoutMapper.toEntity(workoutDTO);
        workout = workoutRepository.save(workout);
        return workoutMapper.toDto(workout);
    }

    @Override
    public Optional<WorkoutDTO> partialUpdate(WorkoutDTO workoutDTO) {
        log.debug("Request to partially update Workout : {}", workoutDTO);

        return workoutRepository
            .findById(workoutDTO.getId())
            .map(
                existingWorkout -> {
                    workoutMapper.partialUpdate(existingWorkout, workoutDTO);
                    return existingWorkout;
                }
            )
            .map(workoutRepository::save)
            .map(workoutMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutDTO> findAll() {
        log.debug("Request to get all Workouts");
        return workoutRepository.findAll().stream().map(workoutMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutDTO> findOne(Long id) {
        log.debug("Request to get Workout : {}", id);
        return workoutRepository.findById(id).map(workoutMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Workout : {}", id);
        workoutRepository.deleteById(id);
    }
}
