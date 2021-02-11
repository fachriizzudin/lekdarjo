package com.lazuardifachri.bps.lekdarjo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    private long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy="subject")
    private Set<Category> category;

    public Subject(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Subject(String name) {
        this.name = name;
    }

    public Subject() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Category> getCategory() {
        return category;
    }
}
