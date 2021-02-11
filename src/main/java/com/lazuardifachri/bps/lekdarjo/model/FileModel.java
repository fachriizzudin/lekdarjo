package com.lazuardifachri.bps.lekdarjo.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "files")
public class FileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Size(max = 100)
    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "size")
    private double size;

    @Lob
    @Column(name = "bytes")
    private byte[] bytes;

    public FileModel(long id, String name, String type, double size) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
    }

    public FileModel(String name, String type, double size, byte[] bytes) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.bytes = bytes;
    }

    public FileModel(long id, String name, double size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    public FileModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

}
