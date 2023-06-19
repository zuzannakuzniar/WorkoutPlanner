package com.zkuzniar.workoutplanner.service.mapper;

import com.zkuzniar.workoutplanner.domain.*;
import com.zkuzniar.workoutplanner.service.dto.WeekDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface WeekMapper extends EntityMapper<WeekDTO, Week> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WeekDTO toDtoId(Week week);
}
