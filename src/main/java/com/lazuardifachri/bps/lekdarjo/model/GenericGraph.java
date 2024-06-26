package com.lazuardifachri.bps.lekdarjo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

public class GenericGraph<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(precision=8, scale=2)
    private T value;

    @NotNull
    private int year;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "meta_fk", nullable = false)
    private GraphMeta meta;

    public GenericGraph(long id, T value, int year) {
        this.id = id;
        this.value = value;
        this.year = year;
    }

    public GenericGraph() {
    }

    public long getId() {
        return id;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public GraphMeta getMeta() {
        return meta;
    }

    public void setMeta(GraphMeta meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "id=" + id +
                ", value=" + value +
                ", year=" + year +
                ", meta=" + meta +
                '}';
    }
}
