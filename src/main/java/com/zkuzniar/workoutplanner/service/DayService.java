package com.zkuzniar.workoutplanner.service;

import com.zkuzniar.workoutplanner.service.dto.DayDTO;
import java.util.List;
import java.util.Optional;

public interface DayService {

    DayDTO save(DayDTO dayDTO);

    Optional<DayDTO> partialUpdate(DayDTO dayDTO);

    List<DayDTO> findAll();

    Optional<DayDTO> findOne(Long id);

    void delete(Long id);
}
