package com.zkuzniar.workoutplanner;

import com.zkuzniar.workoutplanner.WorkoutPlannerApp;
import com.zkuzniar.workoutplanner.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { WorkoutPlannerApp.class, TestSecurityConfiguration.class })
public @interface IntegrationTest {
}
