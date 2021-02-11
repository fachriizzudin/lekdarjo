package com.lazuardifachri.bps.lekdarjo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lazuardifachri.bps.lekdarjo.serializer.SubjectDeserializer;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "publications")
@JsonInclude(Include.NON_NULL)
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(max = 100)
    private String title;

    @NotNull
    @JsonProperty(value = "catalog_no")
    @Size(max = 16)
    @Column(name = "catalog_no")
    private String catalogNo;

    @NotNull
    @JsonProperty(value = "publication_no")
    @Size(max = 11)
    @Column(name = "publication_no")
    private String publicationNo;

    @JsonProperty(value = "issn_or_isbn")
    @Column(name = "issn_or_isbn")
    private String issnOrIsbn;

    @NotNull
    @JsonProperty(value = "release_date")
    @Temporal(TemporalType.DATE)
    @Column(name = "release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date releaseDate;

    @Lob
    @Column(name = "information")
    private String information;

    @NotNull
    @JsonProperty(value = "district")
    @Enumerated(EnumType.STRING)
    private EDistrict district;

    @NotNull
    @JsonDeserialize(using = SubjectDeserializer.class)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_fk", nullable = false)
    private Subject subject;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_fk", referencedColumnName = "id")
    private FileModel image;

    @JsonProperty(value = "image_uri")
    @Column(name = "image_uri")
    private String imageUri;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "document_fk", referencedColumnName = "id")
    private FileModel document;

    @JsonProperty(value = "document_uri")
    @Column(name = "document_uri")
    private String documentUri;

    // untuk mengembalikan detail data beserta bytes image
    public Publication(long id, String title, String catalogNo, String publicationNo, String issnOrIsbn,
            Date releaseDate, String information, EDistrict district, Subject subject, FileModel image, String imageUri,
            String documentUri) {
        this.id = id;
        this.title = title;
        this.catalogNo = catalogNo;
        this.publicationNo = publicationNo;
        this.issnOrIsbn = issnOrIsbn;
        this.releaseDate = releaseDate;
        this.information = information;
        this.district = district;
        this.subject = subject;
        this.image = image;
        this.imageUri = imageUri;
        this.documentUri = documentUri;
    }

    // untuk memgambil data publikasi dari repositori menggunakan hql
    public Publication(long id, String title, String catalogNo, String publicationNo, String issnOrIsbn,
            Date releaseDate, String information, EDistrict district, Subject subject, String imageUri,
            String documentUri) {
        this.id = id;
        this.title = title;
        this.catalogNo = catalogNo;
        this.publicationNo = publicationNo;
        this.issnOrIsbn = issnOrIsbn;
        this.releaseDate = releaseDate;
        this.information = information;
        this.district = district;
        this.subject = subject;
        this.imageUri = imageUri;
        this.documentUri = documentUri;
    }

    // untuk pembuatan objek di publication service
    public Publication(String title, String catalogNo, String publicationNo, String issnOrIsbn, Date releaseDate,
            String information, EDistrict district, Subject subject, FileModel image, FileModel document) {
        this.title = title;
        this.catalogNo = catalogNo;
        this.publicationNo = publicationNo;
        this.issnOrIsbn = issnOrIsbn;
        this.releaseDate = releaseDate;
        this.information = information;
        this.district = district;
        this.subject = subject;
        this.image = image;
        this.document = document;
    }

    public Publication(long id, String title, String catalogNo, String publicationNo, String issnOrIsbn,
            Date releaseDate, String information, String imageUri, String documentUri) {
        this.id = id;
        this.title = title;
        this.catalogNo = catalogNo;
        this.publicationNo = publicationNo;
        this.issnOrIsbn = issnOrIsbn;
        this.releaseDate = releaseDate;
        this.information = information;
        this.imageUri = imageUri;
        this.documentUri = documentUri;
    }

    public Publication(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Publication() {
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

    public String getCatalogNo() {
        return catalogNo;
    }

    public void setCatalogNo(String catalogNo) {
        this.catalogNo = catalogNo;
    }

    public String getPublicationNo() {
        return publicationNo;
    }

    public void setPublicationNo(String publicationNo) {
        this.publicationNo = publicationNo;
    }

    public String getIssnOrIsbn() {
        return issnOrIsbn;
    }

    public void setIssnOrIsbn(String issnOrIsbn) {
        this.issnOrIsbn = issnOrIsbn;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public EDistrict getDistrict() {
        return district;
    }

    public void setDistrict(EDistrict district) {
        this.district = district;
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

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", title='" + getTitle() + "'" + ", catalogNo='" + getCatalogNo() + "'"
                + ", publicationNo='" + getPublicationNo() + "'" + ", issnOrIsbn='" + getIssnOrIsbn() + "'"
                + ", releaseDate='" + getReleaseDate() + "'" + ", information='" + getInformation() + "'"
                + ", district='" + getDistrict() + "'" + ", subject='" + getSubject() + "'" + ", image='" + getImage()
                + "'" + ", imageUri='" + getImageUri() + "'" + ", document='" + getDocument() + "'" + ", documentUri='"
                + getDocumentUri() + "'" + "}";
    }

    public String apiString() {
        return "{" + "\"title\":\"" + getTitle() + "\"" + ", \"catalog_no\":\""
                + getCatalogNo() + "\"" + ", \"publication_no\":\"" + getPublicationNo() + "\""
                + ", \"issn_or_isbn\":\"" + getIssnOrIsbn() + "\"" + ", \"release_date\":\"" +  getFormattedReleaseDate() + "\""
                + ", \"information\":\"" + getInformation() + "\"" + ", \"subject\":" + getSubject().getId()
                + ", \"district\":\"" + getDistrict().getCode() + "\"" + "}";
    }
}
