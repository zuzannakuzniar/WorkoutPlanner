package com.zkuzniar.workoutplanner.service;

import com.zkuzniar.workoutplanner.service.dto.WeekDTO;
import java.util.List;
import java.util.Optional;

public interface WeekService {
    WeekDTO save(WeekDTO weekDTO);

    Optional<WeekDTO> partialUpdate(WeekDTO weekDTO);

    List<WeekDTO> findAll();

    Optional<WeekDTO> findOne(Long id);

    void delete(Long id);
}
