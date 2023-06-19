package com.zkuzniar.workoutplanner.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "week")
public class Week implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "week_of_year")
    private Integer weekOfYear;

    @OneToMany(mappedBy = "week")
    @JsonIgnoreProperties(value = { "week" }, allowSetters = true)
    private Set<Day> days = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Week id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getWeekOfYear() {
        return this.weekOfYear;
    }

    public Week weekOfYear(Integer weekOfYear) {
        this.weekOfYear = weekOfYear;
        return this;
    }

    public void setWeekOfYear(Integer weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public Set<Day> getDays() {
        return this.days;
    }

    public Week days(Set<Day> days) {
        this.setDays(days);
        return this;
    }

    public Week addDay(Day day) {
        this.days.add(day);
        day.setWeek(this);
        return this;
    }

    public Week removeDay(Day day) {
        this.days.remove(day);
        day.setWeek(null);
        return this;
    }

    public void setDays(Set<Day> days) {
        if (this.days != null) {
            this.days.forEach(i -> i.setWeek(null));
        }
        if (days != null) {
            days.forEach(i -> i.setWeek(this));
        }
        this.days = days;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Week)) {
            return false;
        }
        return id != null && id.equals(((Week) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Week{" +
            "id=" + getId() +
            ", weekOfYear=" + getWeekOfYear() +
            "}";
    }
}
