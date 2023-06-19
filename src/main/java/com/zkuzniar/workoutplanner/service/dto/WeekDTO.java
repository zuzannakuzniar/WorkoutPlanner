package com.zkuzniar.workoutplanner.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class WeekDTO implements Serializable {

    private Long id;

    private Integer weekOfYear;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(Integer weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeekDTO)) {
            return false;
        }

        WeekDTO weekDTO = (WeekDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, weekDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeekDTO{" +
            "id=" + getId() +
            ", weekOfYear=" + getWeekOfYear() +
            "}";
    }
}
