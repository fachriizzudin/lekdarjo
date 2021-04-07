package com.lazuardifachri.bps.lekdarjo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lazuardifachri.bps.lekdarjo.serializer.SubjectDeserializer;
import com.lazuardifachri.bps.lekdarjo.validation.DoubleQuoteConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.Set;

@Entity
@Table(name = "graph_meta")
public class GraphMeta {

    @Id
    private long id;

    @NotEmpty
    @DoubleQuoteConstraint
    private String title;


    @JsonDeserialize(using = SubjectDeserializer.class)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_fk", nullable = false)
    private Subject subject;

    @NotEmpty
    @DoubleQuoteConstraint
    private String horizontal;

    @NotEmpty
    @DoubleQuoteConstraint
    private String vertical;

    @NotEmpty
    @JsonProperty(value = "vertical_unit")
    @Column(name = "vertical_unit")
    @DoubleQuoteConstraint
    private String verticalUnit;

    @Lob
    @NotEmpty
    @DoubleQuoteConstraint
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy="meta", cascade = CascadeType.ALL)
    private Set<Graph> graphs;

    public GraphMeta() {
    }

    public GraphMeta(long id, String title, Subject subject, String horizontal, String vertical, String verticalUnit, String description) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.verticalUnit = verticalUnit;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(String horizontal) {
        this.horizontal = horizontal;
    }

    public String getVertical() {
        return vertical;
    }

    public void setVertical(String vertical) {
        this.vertical = vertical;
    }

    public String getVerticalUnit() {
        return verticalUnit;
    }

    public void setVerticalUnit(String verticalUnit) {
        this.verticalUnit = verticalUnit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", subject='" + getSubject().getId() + "'" +
            ", horizontal='" + getHorizontal() + "'" +
            ", vertical='" + getVertical() + "'" +
            ", verticalUnit='" + getVerticalUnit() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }

    public String apiString() {
        return "{" +
            " \"id\":\"" + getId() + "\"" +
            ", \"title\":\"" + getTitle() + "\"" +
            ", \"subject\":" + getSubject().getId() + 
            ", \"horizontal\":\"" + getHorizontal() + "\"" +
            ", \"vertical\":\"" + getVertical() + "\"" +
            ", \"vertical_unit\":\"" + getVerticalUnit() + "\"" +
            ", \"description\":\"" + getDescription() + "\"" +
            "}";
    }

}
