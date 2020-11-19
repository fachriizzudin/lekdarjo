package com.lazuardifachri.bps.lekdarjo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.Graph;
import com.lazuardifachri.bps.lekdarjo.model.GraphMeta;
import com.lazuardifachri.bps.lekdarjo.repository.GraphMetaRepository;
import com.lazuardifachri.bps.lekdarjo.repository.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/graph")
@CrossOrigin(origins = "http://localhost:8080")
public class GraphController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GraphRepository graphRepository;

    @Autowired
    GraphMetaRepository graphMetaRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addGraphData(@RequestPart(name = "id") String id,
                                               @RequestPart(name = "data") String dataJson) throws JsonProcessingException {

        Optional<GraphMeta> graphMeta = graphMetaRepository.findById(Long.valueOf(id));

        if (graphMeta.isPresent()) {
            GraphMeta meta = graphMeta.get();
            Graph data = objectMapper.readValue(dataJson, Graph.class);

            data.setMeta(meta);
            graphRepository.save(data);

            return new ResponseEntity<>("Created", HttpStatus.OK);
        }

        throw new ResourceNotFoundException(ExceptionMessage.GRAPH_NOT_FOUND);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Map<String, Object>> getAllGraphData(@PathVariable String id) {

        Optional<GraphMeta> graphMeta = graphMetaRepository.findById(Long.valueOf(id));

        Map<String, Object> response = new HashMap<>();

        if (graphMeta.isPresent()) {
            GraphMeta meta = graphMeta.get();

            List<Graph> data = graphRepository.findAllByGraphMeta(Long.valueOf(id));

            response.put("data", data);
            response.put("meta", meta);

            return new ResponseEntity<>(response, HttpStatus.OK);

        }

        throw new ResourceNotFoundException(ExceptionMessage.GRAPH_NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateGraphData(@RequestPart(name = "id") String id,
                                                  @RequestPart(name = "data_id") String dataId,
                                                  @RequestPart(name = "data") String dataJson) throws JsonProcessingException {

        Optional<GraphMeta> graphMeta = graphMetaRepository.findById(Long.valueOf(id));

        if (graphMeta.isPresent()) {

            GraphMeta meta = graphMeta.get();
            Optional<Graph> graphData =  graphRepository.findById(Long.valueOf(dataId));

            if (graphData.isPresent()) {
                Graph data = graphData.get();
                Graph newData = objectMapper.readValue(dataJson, Graph.class);
                data.setValue(newData.getValue());
                data.setYear(newData.getYear());
                data.setMeta(meta);

                graphRepository.save(data);

                return new ResponseEntity<>("Updated", HttpStatus.OK);
            }

            throw new ResourceNotFoundException(ExceptionMessage.DATA_GRAPH_NOT_FOUND);
        }

        throw new ResourceNotFoundException(ExceptionMessage.GRAPH_NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "")
    public ResponseEntity<String> deleteGraphData(@RequestParam(name = "id") String id,
                                                  @RequestParam(name = "data_id") String dataId) throws JsonProcessingException {

        Optional<GraphMeta> graphMeta = graphMetaRepository.findById(Long.valueOf(id));

        if (graphMeta.isPresent()) {
            graphRepository.deleteById(Long.valueOf(dataId));
            return new ResponseEntity<>("Deleted", HttpStatus.ACCEPTED);
        } else {
            throw new ResourceNotFoundException(ExceptionMessage.DATA_GRAPH_NOT_FOUND);
        }


    }

}
