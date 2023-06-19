package com.zkuzniar.workoutplanner.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WeekMapperTest {

    private WeekMapper weekMapper;

    @BeforeEach
    public void setUp() {
        weekMapper = new WeekMapperImpl();
    }
}
