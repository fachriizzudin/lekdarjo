package com.lazuardifachri.bps.lekdarjo.model;

public enum EStatType {
    STATISTIK_DASAR(0, "Statistik Dasar"),
    STATISTIK_SEKTORAL(1, "Statistik Sektoral");

    private int code;
    private String name;

    EStatType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
