package com.lazuardifachri.bps.lekdarjo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.Graph;
import com.lazuardifachri.bps.lekdarjo.model.GraphMeta;
import com.lazuardifachri.bps.lekdarjo.repository.GraphMetaRepository;
import com.lazuardifachri.bps.lekdarjo.repository.GraphRepository;
import com.lazuardifachri.bps.lekdarjo.service.GraphMetaService;
import com.lazuardifachri.bps.lekdarjo.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/api/graph")
public class GraphController {

    @Autowired
    GraphService graphService;

    @Autowired
    GraphMetaService graphMetaService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/bulk", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addAllGraphData(@RequestPart(name = "id") String id,
                                                  @RequestPart(name = "data") String dataJson) throws IOException, ParseException {

        graphService.createGraphBulk(dataJson, id);

        return new ResponseEntity<>("Created", HttpStatus.OK);
    }




    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addGraphData(@RequestPart(name = "id") String id,
                                               @RequestPart(name = "data") String dataJson) throws IOException, ParseException {

        graphService.createGraph(dataJson, id);

        return new ResponseEntity<>("Created", HttpStatus.OK);

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Map<String, Object>> getAllGraphData(@PathVariable String id) {

        List<Graph> data = graphService.readAllGraph(id);

        GraphMeta graphMeta = graphMetaService.readGraphMetaByid(id);

        Map<String, Object> response = new HashMap<>();

        response.put("data", data);
        response.put("meta", graphMeta);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateGraphData(@RequestPart(name = "id") String id,
                                                  @RequestPart(name = "data_id") String dataId,
                                                  @RequestPart(name = "data") String dataJson) throws IOException, ParseException {

        graphService.updateGraph(dataId, dataJson, id);

        return new ResponseEntity<>("Updated", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "")
    public ResponseEntity<String> deleteGraphData(@RequestParam(name = "data_id") String dataId) throws JsonProcessingException {
        graphService.deleteGraph(dataId);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
