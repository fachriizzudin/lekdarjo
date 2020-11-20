package com.lazuardifachri.bps.lekdarjo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lazuardifachri.bps.lekdarjo.exception.BadRequestException;

import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EDistrict {
    @JsonProperty("3515010")
    TARIK("3515010", "Tarik"),
    @JsonProperty("3515020")
    PRAMBON("3515020", "Prambon"),
    @JsonProperty("3515030")
    KREMBUNG("3515030", "Krembung"),
    @JsonProperty("3515040")
    PORONG("3515040", "Porong"),
    @JsonProperty("3515050")
    JABON("3515050", " Jabon"),
    @JsonProperty("3515060")
    TANGGULANGIN("3515060", "Tanggulangin"),
    @JsonProperty("3515070")
    CANDI("3515070", "Candi"),
    @JsonProperty("3515080")
    TULANGAN("3515080", "Tulangan"),
    @JsonProperty("3515090")
    WONOAYU("3515090", "Wonoayu"),
    @JsonProperty("3515100")
    SUKODONO("3515100", "Sukodono"),
    @JsonProperty("3515110")
    SIDOARJO("3515110", "Sidoarjo"),
    @JsonProperty("3515120")
    BUDURAN("3515120", "Buduran"),
    @JsonProperty("3515130")
    SEDATI("3515130", "Sedati"),
    @JsonProperty("3515140")
    WARU("3515140", "Waru"),
    @JsonProperty("3515150")
    GEDANGAN("3515150", "Gedangan"),
    @JsonProperty("3515160")
    TAMAN("3515160", "Taman"),
    @JsonProperty("3515170")
    KRIAN("3515170", "Krian"),
    @JsonProperty("3515180")
    BALONG_BANDO("3515180", "Balong Bendo"),
    @JsonProperty("3515")
    UMUM("3515", "Umum");

    private String code;
    private String name;

    private EDistrict(String code) {
        this.code = code;

    }

    EDistrict(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static EDistrict of(String code) {
        if (code == null){
            throw new BadRequestException(code);
        }
        return Stream.of(EDistrict.values())
                .filter(p -> p.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
