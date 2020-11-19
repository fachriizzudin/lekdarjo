package com.lazuardifachri.bps.lekdarjo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "graph_meta")
public class GraphMeta {

    @Id
    private long id;

    @NotNull
    private String title;

    @NotNull
    private String horizontal;

    @NotNull
    private String vertical;

    @NotNull
    private String verticalUnit;

    public GraphMeta() {
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
}
