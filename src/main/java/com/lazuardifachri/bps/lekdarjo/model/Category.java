package com.lazuardifachri.bps.lekdarjo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_fk", nullable = false)
    private Subject subject;

    public Category() {
    }

    public Category(String name, Subject subject) {
        this.name = name;
        this.subject = subject;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
