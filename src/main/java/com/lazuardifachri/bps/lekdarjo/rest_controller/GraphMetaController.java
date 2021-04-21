package com.lazuardifachri.bps.lekdarjo.rest_controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import com.lazuardifachri.bps.lekdarjo.model.GraphMeta;
import com.lazuardifachri.bps.lekdarjo.repository.GraphMetaRepository;
import com.lazuardifachri.bps.lekdarjo.service.FileStorageService;
import com.lazuardifachri.bps.lekdarjo.service.GraphMetaService;
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
@RequestMapping("/api/graphmeta")
@CrossOrigin(origins = "http://localhost:8080")
public class GraphMetaController {

    @Autowired
    GraphMetaService graphMetaService;

    @Autowired
    FileStorageService fileStorageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addGraphMeta(@RequestPart("meta") String metaJson, @RequestPart("image") MultipartFile image) throws IOException, ParseException {
        graphMetaService.createGraphMeta(metaJson, image);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "")
    public ResponseEntity<List<GraphMeta>> getAllGraphMeta(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "3") int size) {
        List<GraphMeta> graphMetas  = graphMetaService.readAllGraphMeta();
        return new ResponseEntity<>(graphMetas, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/count")
    public ResponseEntity<Long> getGraphMetaCount() {
        Long graphMetasCount  = graphMetaService.getGraphMetaCount();
        return new ResponseEntity<>(graphMetasCount, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<GraphMeta> getGraphMetaById(@PathVariable String id) {
        GraphMeta graphMeta = graphMetaService.readGraphMetaByid(id);
        return new ResponseEntity<>(graphMeta, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateGraphData(@RequestPart(name = "id") String id, @RequestPart(name = "meta") String metaJson, @RequestPart("image") MultipartFile image) throws IOException, ParseException {
        graphMetaService.updateGraphMeta(id, metaJson, image);
        return new ResponseEntity<>("Updated", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "")
    public ResponseEntity<String> deleteGraphData(@RequestParam(name = "id") String id) throws JsonProcessingException {
        graphMetaService.deleteGraphMeta(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/images/{id}")
    public ResponseEntity<byte[]> getGraphMetaImage(@PathVariable String id) {

        FileModel indicatorFile = fileStorageService.readFileById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + indicatorFile.getName() + "\"");
        headers.add("Content-Type", indicatorFile.getType());
        return new ResponseEntity<>(indicatorFile.getBytes(), headers, HttpStatus.OK);
    }

}
