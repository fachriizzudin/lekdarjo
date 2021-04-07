package com.lazuardifachri.bps.lekdarjo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lazuardifachri.bps.lekdarjo.serializer.CategoryDeserializer;

import com.lazuardifachri.bps.lekdarjo.validation.DoubleQuoteConstraint;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;



@Entity
@Table(name = "indicators")
public class Indicator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    @DoubleQuoteConstraint
    private String title;

    @NotNull
    @JsonProperty(value = "release_date")
    @Temporal(TemporalType.DATE)
    @Column(name = "release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date releaseDate;

    @NotNull
    @JsonDeserialize(using = CategoryDeserializer.class)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_fk", nullable = false)
    private Category category;

    @NotNull
    @JsonProperty(value = "stat_type")
    @Enumerated(EnumType.STRING)
    private EStatType statType;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "document_fk", referencedColumnName = "id")
    private FileModel document;

    @JsonProperty(value = "document_uri")
    @Column(name = "document_uri")
    private String documentUri;

    public Indicator(long id, String title, Date releaseDate, Category category, EStatType statType, String documentUri) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.category = category;
        this.statType = statType;
        this.documentUri = documentUri;
    }

    public Indicator(String title, Date releaseDate, Category category, EStatType statType) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.category = category;
        this.statType = statType;
    }

    public Indicator() {
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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public EStatType getStatType() {
        return statType;
    }

    public void setStatType(EStatType statType) {
        this.statType = statType;
    }

    public FileModel getDocument() {
        return document;
    }

    public void setDocument(FileModel document) {
        this.document = document;
    }

    public String getDocumentUri() {
        return documentUri;
    }

    public void setDocumentUri(String documentUri) {
        this.documentUri = documentUri;
    }

    public String getFormattedReleaseDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(this.releaseDate);
    }

    public String apiString() {
        return "{" + "\"title\":\"" + getTitle() + "\"" 
                + ", \"release_date\":\"" + getFormattedReleaseDate() + "\"" 
                + ", \"category\":\"" + getCategory().getId() + "\"" 
                + ", \"stat_type\":" + getStatType().getCode() + "}";
    }
}
