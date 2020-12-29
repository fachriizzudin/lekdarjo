package com.lazuardifachri.bps.lekdarjo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.Graph;
import com.lazuardifachri.bps.lekdarjo.model.GraphMeta;
import com.lazuardifachri.bps.lekdarjo.repository.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class GraphServiceImpl implements GraphService{

    @Autowired
    GraphMetaService graphMetaService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GraphRepository graphRepository;

    @Override
    public List<Graph> createGraphBulk(String graphJson, String metaId) throws IOException, ParseException {

        GraphMeta graphMeta = graphMetaService.readGraphMetaByid(metaId);

        Graph[] data = objectMapper.readValue(graphJson, Graph[].class);

        for (Graph graphData : data) {
            graphData.setMeta(graphMeta);
        }

        return graphRepository.saveAll(Arrays.asList(data));
    }

    @Override
    public Graph createGraph(String graphJson, String metaId) throws IOException, ParseException {

        GraphMeta graphMeta = graphMetaService.readGraphMetaByid(metaId);

        Graph data = objectMapper.readValue(graphJson, Graph.class);
        data.setMeta(graphMeta);

        return graphRepository.save(data);
    }

    @Override
    public List<Graph> readAllGraph(String metaId) {
        return graphRepository.findAllByGraphMeta(Long.parseLong(metaId));
    }

    @Override
    public Graph readById(String dataId) {
        Optional<Graph> graphOptional = graphRepository.findById(Double.parseDouble(dataId));
        return graphOptional.orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.GRAPH_NOT_FOUND));
    }

    @Override
    public Graph updateGraph(String dataId, String dataJson, String metaId) throws IOException, ParseException {

        GraphMeta graphMeta = graphMetaService.readGraphMetaByid(metaId);

        Graph graph = readById(dataId);

        Graph newGraph = objectMapper.readValue(dataJson, Graph.class);
        graph.setValue(newGraph.getValue());
        graph.setYear(newGraph.getYear());
        graph.setMeta(graphMeta);

        return graphRepository.save(graph);
    }

    @Override
    public void deleteGraph(String dataId) {
        graphRepository.deleteById(Double.parseDouble(dataId));
    }
}
