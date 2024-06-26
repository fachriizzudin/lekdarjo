package com.lazuardifachri.bps.lekdarjo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lazuardifachri.bps.lekdarjo.validation.GraphDataConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "graph")
public class Graph {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(precision=8, scale=2)
    private Double value;

    @NotNull
    private int year;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "meta_fk", nullable = false)
    private GraphMeta meta;

    public Graph(long id, Double value,int year) {
        this.id = id;
        this.value = value;
        this.year = year;
    }

    public Graph(long id, @NotNull Double value, @NotNull int year, GraphMeta meta) {
        this.id = id;
        this.value = value;
        this.year = year;
        this.meta = meta;
    }

    public Graph() {
    }

    public long getId() {
        return id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
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
