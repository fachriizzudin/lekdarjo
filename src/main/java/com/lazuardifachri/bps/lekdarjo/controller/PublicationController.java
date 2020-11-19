package com.lazuardifachri.bps.lekdarjo.controller;

import com.lazuardifachri.bps.lekdarjo.Utils;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.EDistrict;
import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import com.lazuardifachri.bps.lekdarjo.model.Publication;
import com.lazuardifachri.bps.lekdarjo.service.FileStorageService;
import com.lazuardifachri.bps.lekdarjo.service.PublicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/api/publications")
@CrossOrigin(origins = "http://localhost:8080")
public class PublicationController {

    Logger log = LoggerFactory.getLogger(PublicationController.class);

    @Autowired
    PublicationService publicationService;

    @Autowired
    FileStorageService fileStorageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addPublication(@RequestPart("pub") String pubJson, @RequestPart("file") MultipartFile pubFile) throws IOException, ParseException {
        publicationService.createPublication(pubJson, pubFile);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "")
    public ResponseEntity<Map<String, Object>> getAllPublication(@RequestParam(name = "district", required = false) Optional<String> districtCode,
                                                               @RequestParam(name = "year", required = false) Optional<String> year,
                                                               @RequestParam(name = "subject", required = false) Optional<String> subjectId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "3") int size){


        List<Publication> publications = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);
        Page<Publication> pagePub = null;

        int switchvar = 0;

        if (districtCode.isPresent()) {switchvar += 1;}
        if (year.isPresent()) {switchvar += 10;}
        if (subjectId.isPresent()) {switchvar += 100;}

        EDistrict eDistrict;

        switch (switchvar) {
            case (0):
                pagePub = publicationService.readAllPublication(paging);
                break;
            case (1):
                eDistrict = Utils.getEDistrictByCode(districtCode.get());
                pagePub = publicationService.readPublicationByDistrict(eDistrict, paging);
                break;
            case (10):
                pagePub = publicationService.readPublicationByYear(year.get(), paging);
                break;
            case (11):
                eDistrict = Utils.getEDistrictByCode(districtCode.get());
                pagePub = publicationService.readPublicationByDistrictAndYear(eDistrict, year.get(), paging);
                break;
            case (100):
                pagePub = publicationService.readPublicationBySubject(subjectId.get(), paging);
                break;
            case (110):
                pagePub = publicationService.readPublicationBySubjectAndYear(subjectId.get(), year.get(), paging);
                break;
            default: throw new ResourceNotFoundException(ExceptionMessage.FILTER_NOT_SUPPORTED);
        }

        publications = pagePub.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("publications", publications);
        response.put("current_page", pagePub.getNumber());
        response.put("total_items", pagePub.getTotalElements());
        response.put("total_pages", pagePub.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Publication> getPublicationById(@PathVariable String id){
        Publication publication = publicationService.readPublicationByid(id);
        return new ResponseEntity<>(publication, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/files/{id}")
    public ResponseEntity<byte[]> getPublicationFile(@PathVariable String id) {

        FileModel publicationFile = fileStorageService.readFileById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + publicationFile.getName() + "\"");

        return new ResponseEntity<>(publicationFile.getBytes(), headers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/images/{id}")
    public ResponseEntity<byte[]> getPublicationImage(@PathVariable String id) {

        FileModel publicationImage = fileStorageService.readFileById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);

        return new ResponseEntity<>(publicationImage.getBytes(), headers, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Publication> updatePublication(@PathVariable String id, @RequestPart("pub") String pubJson, @RequestPart(value = "file", required = false) MultipartFile pubFile) throws IOException, ParseException {
        Publication publication = publicationService.updatePublication(id, pubJson, pubFile);
        return new ResponseEntity<>(publication, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> removePublication(@PathVariable String id) {
        publicationService.deletePublication(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
