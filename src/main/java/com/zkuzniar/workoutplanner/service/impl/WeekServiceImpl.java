package com.zkuzniar.workoutplanner.service.impl;

import com.zkuzniar.workoutplanner.domain.Week;
import com.zkuzniar.workoutplanner.repository.WeekRepository;
import com.zkuzniar.workoutplanner.service.WeekService;
import com.zkuzniar.workoutplanner.service.dto.WeekDTO;
import com.zkuzniar.workoutplanner.service.mapper.WeekMapper;
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
public class WeekServiceImpl implements WeekService {

    private final Logger log = LoggerFactory.getLogger(WeekServiceImpl.class);

    private final WeekRepository weekRepository;

    private final WeekMapper weekMapper;

    public WeekServiceImpl(WeekRepository weekRepository, WeekMapper weekMapper) {
        this.weekRepository = weekRepository;
        this.weekMapper = weekMapper;
    }

    @Override
    public WeekDTO save(WeekDTO weekDTO) {
        log.debug("Request to save Week : {}", weekDTO);
        Week week = weekMapper.toEntity(weekDTO);
        week = weekRepository.save(week);
        return weekMapper.toDto(week);
    }

    @Override
    public Optional<WeekDTO> partialUpdate(WeekDTO weekDTO) {
        log.debug("Request to partially update Week : {}", weekDTO);

        return weekRepository
            .findById(weekDTO.getId())
            .map(
                existingWeek -> {
                    weekMapper.partialUpdate(existingWeek, weekDTO);
                    return existingWeek;
                }
            )
            .map(weekRepository::save)
            .map(weekMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WeekDTO> findAll() {
        log.debug("Request to get all Weeks");
        return weekRepository.findAll().stream().map(weekMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WeekDTO> findOne(Long id) {
        log.debug("Request to get Week : {}", id);
        return weekRepository.findById(id).map(weekMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Week : {}", id);
        weekRepository.deleteById(id);
    }
}
