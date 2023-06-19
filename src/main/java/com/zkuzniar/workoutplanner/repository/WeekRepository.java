package com.zkuzniar.workoutplanner.repository;

import com.zkuzniar.workoutplanner.domain.Week;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface WeekRepository extends JpaRepository<Week, Long> {}
