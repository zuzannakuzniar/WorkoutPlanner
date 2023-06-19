package com.zkuzniar.workoutplanner.repository;

import com.zkuzniar.workoutplanner.domain.Workout;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    @Query("select workout from Workout workout where workout.user.login = ?#{principal.preferredUsername}")
    List<Workout> findByUserIsCurrentUser();
}
