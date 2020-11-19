package com.lazuardifachri.bps.lekdarjo.service;

import com.lazuardifachri.bps.lekdarjo.model.Indicator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface IndicatorService {

    Indicator createIndicator(String idkJson, MultipartFile file) throws IOException, ParseException;

    Page<Indicator> readAllIndicator(Pageable pageable);

    Indicator readIndicatorByid(String idkId);

    Page<Indicator> readIndicatorByCategory(String categoryId, Pageable pageable);

    Indicator updateIndicator(String idkId, String idkJson, MultipartFile file) throws IOException, ParseException;

    void deleteIndicator(String idkId);
}
