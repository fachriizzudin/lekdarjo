package com.lazuardifachri.bps.lekdarjo.service;

import com.lazuardifachri.bps.lekdarjo.model.StatisticalNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

public interface StatisticalNewsService {

    StatisticalNews createStatisticalNews(String newsJson, MultipartFile file) throws IOException, ParseException;

    Page<StatisticalNews> readAllStatisticalNews(Pageable pageable);

    StatisticalNews readStatisticalNewsById(String newsId);

    Page<StatisticalNews> readStatisticalNewsByCategory(String categoryId, Pageable pageable);

    Page<StatisticalNews> readStatisticalNewsByCategoryAndYear(String categoryId, String year, Pageable pageable);

    Page<StatisticalNews> readStatisticalNewsByCategoryAndYearAndMonth(String categoryId, String year, String month, Pageable pageable);

    Page<StatisticalNews> readStatisticalNewsByYear(String year, Pageable pageable);

    Page<StatisticalNews> readStatisticalNewsByYearAndMonth(String year, String month, Pageable pageable);

    StatisticalNews updateStatisticalNews(String newsId, String newsJson, MultipartFile file) throws IOException, ParseException;

    void deleteStatisticalNews(String newsId);

}
