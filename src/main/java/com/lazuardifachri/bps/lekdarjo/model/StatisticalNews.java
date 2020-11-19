package com.lazuardifachri.bps.lekdarjo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lazuardifachri.bps.lekdarjo.serializer.CategoryDeserializer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name="statistical_news")
public class StatisticalNews {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(max = 100, min = 20)
    @Column(name = "title")
    private String title;

    @NotNull
    @Lob
    @Column(name = "abstraction")
    @Size(min = 120)
    private String abstraction;

    @NotNull
    @JsonProperty(value = "release_date")
    @Temporal(TemporalType.DATE)
    @Column(name = "release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date releaseDate;

    @NotNull
    @JsonDeserialize(using = CategoryDeserializer.class)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_fk", nullable = false)
    private Category category;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_fk", referencedColumnName = "id")
    private FileModel document;

    @JsonProperty(value = "document_uri")
    @Column(name = "document_uri")
    private String documentUri;

    public StatisticalNews(long id, String title, String abstraction, Date releaseDate, Category category, String documentUri) {
        this.id = id;
        this.title = title;
        this.abstraction = abstraction;
        this.releaseDate = releaseDate;
        this.category = category;
        this.documentUri = documentUri;
    }

    public StatisticalNews() {
    }

    public Long getId() {
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

    public String getAbstraction() {
        return abstraction;
    }

    public void setAbstraction(String abstraction) {
        this.abstraction = abstraction;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
}

