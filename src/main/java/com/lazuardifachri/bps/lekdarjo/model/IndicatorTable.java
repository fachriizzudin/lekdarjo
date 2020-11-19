package com.lazuardifachri.bps.lekdarjo.model;

import com.lazuardifachri.bps.lekdarjo.model.indicator.PDRB;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/*
* PENGEMBANGAN SELANJUTNYA, MEMBUAT DATA DINAMIS DARI TABEL INDIKATOR
*
* */

public class IndicatorTable<T> {

    private long id;

    private String title;

    private Category category;

    private List<T> object;

}
