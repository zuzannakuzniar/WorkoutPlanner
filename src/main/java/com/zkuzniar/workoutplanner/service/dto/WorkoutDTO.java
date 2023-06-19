package com.zkuzniar.workoutplanner.service.dto;

import com.zkuzniar.workoutplanner.domain.enumeration.Type;
import java.io.Serializable;
import java.util.Objects;
public class WorkoutDTO implements Serializable {

    private Long id;

    private Type type;

    private Long duration;

    private DayDTO day;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public DayDTO getDay() {
        return day;
    }

    public void setDay(DayDTO day) {
        this.day = day;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutDTO)) {
            return false;
        }

        WorkoutDTO workoutDTO = (WorkoutDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workoutDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", duration=" + getDuration() +
            ", day=" + getDay() +
            ", user='" + getUser() + "'" +
            "}";
    }
}
