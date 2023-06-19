package com.zkuzniar.workoutplanner.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DayMapperTest {

    private DayMapper dayMapper;

    @BeforeEach
    public void setUp() {
        dayMapper = new DayMapperImpl();
    }
}
