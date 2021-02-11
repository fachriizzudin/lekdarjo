package com.lazuardifachri.bps.lekdarjo.rest_controller;

import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import com.lazuardifachri.bps.lekdarjo.model.Indicator;
import com.lazuardifachri.bps.lekdarjo.service.FileStorageService;
import com.lazuardifachri.bps.lekdarjo.service.IndicatorService;
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
import java.util.*;

@RestController
@RequestMapping("/api/indicators")
@CrossOrigin(origins = "http://localhost:8080")
public class IndicatorController {

    @Autowired
    IndicatorService indicatorService;

    @Autowired
    FileStorageService fileStorageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addIndicator(@RequestPart("idk") String idkJson, @RequestPart("file") MultipartFile file) throws IOException, ParseException {
        indicatorService.createIndicator(idkJson, file);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "")
    public ResponseEntity<Map<String, Object>> getAllIndicator(@RequestParam(name = "category", required = false) Optional<String> categoryId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "3") int size) {

        List<Indicator> indicators = new ArrayList<>();
        Pageable wholePage = Pageable.unpaged();
        Page<Indicator> pageIdk = null;

        if (categoryId.isPresent()) {
            pageIdk = indicatorService.readIndicatorByCategory(categoryId.get(), wholePage);
        } else {
            pageIdk = indicatorService.readAllIndicator(wholePage);
        }

        indicators = pageIdk.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("indicators", indicators);
        response.put("current_page", pageIdk.getNumber());
        response.put("total_items", pageIdk.getTotalElements());
        response.put("total_pages", pageIdk.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Indicator> getIndicatorById(@PathVariable String id){
        Indicator indicator = indicatorService.readIndicatorByid(id);
        return new ResponseEntity<>(indicator, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/files/{id}")
    public ResponseEntity<byte[]> getIndicatorImage(@PathVariable String id) {

        FileModel indicatorFile = fileStorageService.readFileById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + indicatorFile.getName() + "\"");

        return new ResponseEntity<>(indicatorFile.getBytes(), headers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateIndicator(@RequestPart("id") String id, @RequestPart("idk") String idkJson, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException, ParseException {
        indicatorService.updateIndicator(id, idkJson, file);
        return new ResponseEntity<>("Updated", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "")
    public ResponseEntity<HttpStatus> removeIndicator(@RequestParam("id") String id) {
        indicatorService.deleteIndicator(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


}
