package com.zkuzniar.workoutplanner.service.mapper;

import com.zkuzniar.workoutplanner.domain.*;
import com.zkuzniar.workoutplanner.service.dto.WorkoutDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { DayMapper.class, UserMapper.class })
public interface WorkoutMapper extends EntityMapper<WorkoutDTO, Workout> {
    @Mapping(target = "day", source = "day", qualifiedByName = "id")
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    WorkoutDTO toDto(Workout s);
}
