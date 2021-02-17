package com.lazuardifachri.bps.lekdarjo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lazuardifachri.bps.lekdarjo.serializer.SubjectDeserializer;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "infographics")
public class Infographic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    @NotEmpty
    @Column(name = "title")
    private String title;

    @NotNull
    @JsonDeserialize(using = SubjectDeserializer.class)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_fk", nullable = false)
    private Subject subject;

    @NotNull
    @JsonProperty(value = "release_date")
    @Temporal(TemporalType.DATE)
    @Column(name = "release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date releaseDate;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_fk", referencedColumnName = "id")
    private FileModel image;

    @JsonProperty(value = "image_uri")
    @Column(name = "image_uri")
    private String imageUri;

    public Infographic(long id, String title, Subject subject, Date releaseDate, String imageUri) {
        Id = id;
        this.title = title;
        this.subject = subject;
        this.releaseDate = releaseDate;
        this.imageUri = imageUri;
    }

    public Infographic(String title, Subject subject, Date releaseDate) {
        this.title = title;
        this.subject = subject;
        this.releaseDate = releaseDate;
    }

    public Infographic() {
    }

    public long getId() {
        return Id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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

    public String getFormattedReleaseDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(this.releaseDate);
    }

    public String apiString() {
        return "{" + "\"title\":\"" + getTitle() + "\"" 
                + ", \"release_date\":\"" + getFormattedReleaseDate() + "\"" 
                + ", \"subject\":\"" + getSubject().getId() + "\"" + "}";
    }
}
