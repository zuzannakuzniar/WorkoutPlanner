package com.zkuzniar.workoutplanner.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zkuzniar.workoutplanner.domain.enumeration.Type;
import java.io.Serializable;
import javax.persistence.*;
@Entity
@Table(name = "workout")
public class Workout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "duration")
    private Long duration;

    @JsonIgnoreProperties(value = { "week" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Day day;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Workout id(Long id) {
        this.id = id;
        return this;
    }

    public Type getType() {
        return this.type;
    }

    public Workout type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getDuration() {
        return this.duration;
    }

    public Workout duration(Long duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Day getDay() {
        return this.day;
    }

    public Workout day(Day day) {
        this.setDay(day);
        return this;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public User getUser() {
        return this.user;
    }

    public Workout user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Workout)) {
            return false;
        }
        return id != null && id.equals(((Workout) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Workout{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", duration=" + getDuration() +
            "}";
    }
}
