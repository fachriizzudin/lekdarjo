package com.lazuardifachri.bps.lekdarjo.rest_controller;

import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import com.lazuardifachri.bps.lekdarjo.model.StatisticalNews;
import com.lazuardifachri.bps.lekdarjo.service.FileStorageService;
import com.lazuardifachri.bps.lekdarjo.service.StatisticalNewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/statnews")
@CrossOrigin(origins = "http://localhost:8080")
public class StatisticalNewsController {

    Logger log = LoggerFactory.getLogger(StatisticalNewsController.class);

    @Autowired
    StatisticalNewsService statisticalNewsService;

    @Autowired
    FileStorageService fileStorageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addStatisticalNews(@RequestPart("news") String newsJson, @RequestPart("file") MultipartFile newsFile) throws IOException, ParseException {
        statisticalNewsService.createStatisticalNews(newsJson, newsFile);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "")
    public ResponseEntity<Map<String, Object>> getAllStatisticalNews(@RequestParam(name = "category", required = false) Optional<String> categoryId,
                                                                     @RequestParam(name = "year", required = false) Optional<String> year,
                                                                     @RequestParam(name = "month", required = false) Optional<String> month,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "3") int size){

        List<StatisticalNews> statisticalNews;
        Pageable wholePage = Pageable.unpaged();
        Page<StatisticalNews> pageNews = null;

        int switchvar = 0;

        if (categoryId.isPresent()) {switchvar += 1;}
        if (year.isPresent()) {switchvar += 10;}
        if (month.isPresent()) {switchvar += 100;}


        switch (switchvar) {
            case (0):
                pageNews = statisticalNewsService.readAllStatisticalNews(wholePage);
                break;
            case (1):
                pageNews = statisticalNewsService.readStatisticalNewsByCategory(categoryId.get(), wholePage);
                break;
            case (11):
                pageNews = statisticalNewsService.readStatisticalNewsByCategoryAndYear(categoryId.get(), year.get(), wholePage);
                break;
            case (111):
                pageNews = statisticalNewsService.readStatisticalNewsByCategoryAndYearAndMonth(categoryId.get(), year.get(), month.get(), wholePage);
                break;
            case (10):
                pageNews = statisticalNewsService.readStatisticalNewsByYear(year.get(), wholePage);
                break;
            case (110):
                pageNews = statisticalNewsService.readStatisticalNewsByYearAndMonth(year.get(), month.get(), wholePage);
                break;
            default: throw new ResourceNotFoundException(ExceptionMessage.FILTER_NOT_SUPPORTED);
        }

        statisticalNews = pageNews.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("statistical_news", statisticalNews);
        response.put("current_page", pageNews.getNumber());
        response.put("total_items", pageNews.getTotalElements());
        response.put("total_pages", pageNews.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<StatisticalNews> getStatisticalNewsById(@PathVariable String id){
        StatisticalNews statisticalNews = statisticalNewsService.readStatisticalNewsById(id);
        return new ResponseEntity<>(statisticalNews, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/files/{id}")
    public ResponseEntity<byte[]> getStatisticalNewsFile(@PathVariable String id) {

        FileModel newsFile = fileStorageService.readFileById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + newsFile.getName() + "\"");

        return new ResponseEntity<>(newsFile.getBytes(), headers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateStatisticalNews(@RequestPart("id") String id, @RequestPart("news") String newsJson, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException, ParseException {
        statisticalNewsService.updateStatisticalNews(id, newsJson, file);
        return new ResponseEntity<>("Updated", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "")
    public ResponseEntity<HttpStatus> removeStatisticalNews(@RequestParam("id") String id) {
        statisticalNewsService.deleteStatisticalNews(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
