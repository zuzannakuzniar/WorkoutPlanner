package com.zkuzniar.workoutplanner.service.mapper;

import com.zkuzniar.workoutplanner.domain.*;
import com.zkuzniar.workoutplanner.service.dto.DayDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { WeekMapper.class })
public interface DayMapper extends EntityMapper<DayDTO, Day> {
    @Mapping(target = "week", source = "week", qualifiedByName = "id")
    DayDTO toDto(Day s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DayDTO toDtoId(Day day);
}
