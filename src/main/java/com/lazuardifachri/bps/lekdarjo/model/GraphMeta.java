package com.lazuardifachri.bps.lekdarjo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lazuardifachri.bps.lekdarjo.serializer.SubjectDeserializer;
import com.lazuardifachri.bps.lekdarjo.validation.DoubleQuoteConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.Set;

@Entity
@Table(name = "graph_meta")
public class GraphMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @JsonProperty(value = "serial_number")
    @Column(name = "serial_number")
    private Integer serialNumber;

    @NotEmpty
    @DoubleQuoteConstraint
    private String title;

    @JsonDeserialize(using = SubjectDeserializer.class)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_fk", nullable = false)
    private Subject subject;

    @Value("Tahun")
    private String horizontal;

    @NotEmpty
    @DoubleQuoteConstraint
    private String vertical;

    @JsonProperty(value = "vertical_unit")
    @Column(name = "vertical_unit")
    @DoubleQuoteConstraint
    private String verticalUnit;

    @Lob
    @NotEmpty
    @DoubleQuoteConstraint
    private String description;

    @JsonProperty(value = "graph_type")
    @Column(name = "graph_type")
    private int graphType;

    @JsonProperty(value = "data_type")
    @Column(name = "data_type")
    private int dataType;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_fk", referencedColumnName = "id")
    private FileModel image;

    @Lob
    @JsonProperty(value = "image_uri")
    @Column(name = "image_uri")
    private String imageUri;

    @OneToMany(mappedBy="meta", cascade = CascadeType.ALL)
    private Set<Graph> graphs;

    public GraphMeta() {
    }

    public GraphMeta(long id, Integer serialNumber, @NotEmpty String title, Subject subject, @NotEmpty String horizontal, @NotEmpty String vertical, @NotEmpty String verticalUnit, @NotEmpty String description, int graphType, int dataType, FileModel image, String imageUri, Set<Graph> graphs) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.title = title;
        this.subject = subject;
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.verticalUnit = verticalUnit;
        this.description = description;
        this.graphType = graphType;
        this.dataType = dataType;
        this.image = image;
        this.imageUri = imageUri;
        this.graphs = graphs;
    }

    public GraphMeta(long id, Integer serialNumber, @NotEmpty String title, Subject subject, @NotEmpty String horizontal, @NotEmpty String vertical, @NotEmpty String verticalUnit, @NotEmpty String description, int graphType, int dataType, String imageUri) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.title = title;
        this.subject = subject;
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.verticalUnit = verticalUnit;
        this.description = description;
        this.graphType = graphType;
        this.dataType = dataType;
        this.imageUri = imageUri;
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

    public FileModel getImage() {
        return image;
    }

    public void setImage(FileModel image) {
        this.image = image;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Set<Graph> getGraphs() {
        return graphs;
    }

    public void setGraphs(Set<Graph> graphs) {
        this.graphs = graphs;
    }

    public int getGraphType() {
        return graphType;
    }

    public void setGraphType(int graphType) {
        this.graphType = graphType;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "GraphMeta{" +
                "id=" + id +
                ", serialNumber=" + serialNumber +
                ", title='" + title + '\'' +
                ", subject=" + subject +
                ", horizontal='" + horizontal + '\'' +
                ", vertical='" + vertical + '\'' +
                ", verticalUnit='" + verticalUnit + '\'' +
                ", description='" + description + '\'' +
                ", graphType=" + graphType +
                ", dataType=" + dataType +
                ", image=" + image +
                ", imageUri='" + imageUri + '\'' +
                ", graphs=" + graphs +
                '}';
    }

    public String apiString() {
        return "{" +
                " \"id\":\"" + getId() + "\"" +
                ", \"serial_number\":\"" + getSerialNumber() + "\"" +
                ", \"title\":\"" + getTitle() + "\"" +
                ", \"subject\":" + getSubject().getId() +
                ", \"horizontal\":\"" + getHorizontal() + "\"" +
                ", \"vertical\":\"" + getVertical() + "\"" +
                ", \"vertical_unit\":\"" + getVerticalUnit() + "\"" +
                ", \"description\":\"" + getDescription() + "\"" +
                ", \"graph_type\":\"" + getGraphType() + "\"" +
                ", \"data_type\":\"" + getDataType() + "\"" +
                "}";
    }

    public String apiStringWithUri() {
        return "{" +
                " \"id\":\"" + getId() + "\"" +
                ", \"serial_number\":\"" + getSerialNumber() + "\"" +
                ", \"title\":\"" + getTitle() + "\"" +
                ", \"subject\":" + getSubject().getId() +
                ", \"horizontal\":\"" + getHorizontal() + "\"" +
                ", \"vertical\":\"" + getVertical() + "\"" +
                ", \"vertical_unit\":\"" + getVerticalUnit() + "\"" +
                ", \"description\":\"" + getDescription() + "\"" +
                ", \"graph_type\":\"" + getGraphType() + "\"" +
                ", \"data_type\":\"" + getDataType() + "\"" +
                ", \"image_uri\":\"" + getImageUri() + "\"" +
                "}";
    }
}
