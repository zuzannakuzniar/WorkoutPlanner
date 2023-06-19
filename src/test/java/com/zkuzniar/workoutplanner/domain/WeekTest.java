package com.zkuzniar.workoutplanner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.zkuzniar.workoutplanner.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WeekTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Week.class);
        Week week1 = new Week();
        week1.setId(1L);
        Week week2 = new Week();
        week2.setId(week1.getId());
        assertThat(week1).isEqualTo(week2);
        week2.setId(2L);
        assertThat(week1).isNotEqualTo(week2);
        week1.setId(null);
        assertThat(week1).isNotEqualTo(week2);
    }
}
