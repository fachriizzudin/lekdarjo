package com.lazuardifachri.bps.lekdarjo.rest_controller;

import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import com.lazuardifachri.bps.lekdarjo.model.Infographic;
import com.lazuardifachri.bps.lekdarjo.service.FileStorageService;
import com.lazuardifachri.bps.lekdarjo.service.InfographicService;

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
import java.util.*;

@RestController
@RequestMapping("/api/infographics")
@CrossOrigin(origins = "http://localhost:8080")
public class InfographicController {

    Logger log = LoggerFactory.getLogger(InfographicController.class);

    @Autowired
    InfographicService infographicService;

    @Autowired
    FileStorageService fileStorageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addInfographic(@RequestPart("info") String pubJson, @RequestPart("image") MultipartFile infoImage) throws IOException, ParseException {
        infographicService.createInfographic(pubJson, infoImage);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "")
    public ResponseEntity<List<Infographic>> getAllInfographic(@RequestParam(name = "subject", required = false) Optional<String> subjectId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "3") int size) {

        List<Infographic> infographics = new ArrayList<>();
        Pageable wholePage = Pageable.unpaged();
        Page<Infographic> pageinfo = null;

        if (subjectId.isPresent()) {
            pageinfo = infographicService.readInfographicBySubject(subjectId.get(), wholePage);
        } else {
            pageinfo = infographicService.readAllInfographic(wholePage);
        }

        infographics = pageinfo.getContent();

        return new ResponseEntity<>(infographics, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Infographic> getInfographicById(@PathVariable String id){
        Infographic publication = infographicService.readInfographicByid(id);
        return new ResponseEntity<>(publication, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/images/{id}")
    public ResponseEntity<byte[]> getInfographicImage(@PathVariable String id) {

        FileModel infographicImage = fileStorageService.readFileById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);

        return new ResponseEntity<>(infographicImage.getBytes(), headers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateInfographic(@RequestPart("id") String id, @RequestPart("info") String infoJson, @RequestPart(value = "image", required = false) MultipartFile file) throws IOException, ParseException {
        infographicService.updateInfographic(id, infoJson, file);
        return new ResponseEntity<>("Updated", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "")
    public ResponseEntity<HttpStatus> removeStatisticalNews(@RequestParam("id") String id) {
        infographicService.deleteInfographic(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
