package com.zkuzniar.workoutplanner.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;

@ApiModel()
public class DayDTO implements Serializable {

    private Long id;

    private String name;

    private WeekDTO week;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WeekDTO getWeek() {
        return week;
    }

    public void setWeek(WeekDTO week) {
        this.week = week;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DayDTO)) {
            return false;
        }

        DayDTO dayDTO = (DayDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dayDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DayDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", week=" + getWeek() +
            "}";
    }
}
