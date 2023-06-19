package com.zkuzniar.workoutplanner.repository;

import com.zkuzniar.workoutplanner.domain.Day;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface DayRepository extends JpaRepository<Day, Long> {}
