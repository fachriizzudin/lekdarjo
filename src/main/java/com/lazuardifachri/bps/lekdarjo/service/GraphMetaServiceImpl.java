package com.lazuardifachri.bps.lekdarjo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.GraphMeta;
import com.lazuardifachri.bps.lekdarjo.repository.GraphMetaRepository;
import com.lazuardifachri.bps.lekdarjo.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

@Transactional
@Service
public class GraphMetaServiceImpl implements GraphMetaService{

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ValidationService validationService;

    @Autowired
    GraphMetaRepository graphMetaRepository;

    @Override
    public GraphMeta createGraphMeta(String graphJson) throws IOException, ParseException {
        GraphMeta graphMeta = objectMapper.readValue(graphJson, GraphMeta.class);
        return graphMetaRepository.save(graphMeta);
    }

    @Override
    public Page<GraphMeta> readAllGraphMeta(Pageable pageable) {
        return graphMetaRepository.findAll(pageable);
    }

    @Override
    public GraphMeta readGraphMetaByid(String metaId) {
        Optional<GraphMeta> graphMeta = graphMetaRepository.findById(Long.parseLong(metaId));
        return graphMeta.orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.GRAPH_NOT_FOUND));
    }

    @Override
    public GraphMeta updateGraphMeta(String metaId, String metaJson) throws IOException, ParseException {
        Optional<GraphMeta> graphMetaOptional = graphMetaRepository.findById(Long.parseLong(metaId));
        GraphMeta newGraphMeta = objectMapper.readValue(metaJson, GraphMeta.class);
        GraphMeta graphMeta;
        if (graphMetaOptional.isPresent()) {
            graphMeta = graphMetaOptional.get();
            graphMeta.setTitle(newGraphMeta.getTitle());
            graphMeta.setSubject(newGraphMeta.getSubject());
            graphMeta.setHorizontal(newGraphMeta.getHorizontal());
            graphMeta.setVertical(newGraphMeta.getVertical());
            graphMeta.setVerticalUnit(newGraphMeta.getVerticalUnit());
            graphMeta.setDescription(newGraphMeta.getDescription());

            validationService.validateGraphMeta(graphMeta);

            return graphMetaRepository.save(graphMeta);
        } else {
            throw  new ResourceNotFoundException(ExceptionMessage.GRAPH_NOT_FOUND);
        }
    }

    @Override
    public void deleteGraphMeta(String metaId) {
        try {
            graphMetaRepository.deleteById(Long.parseLong(metaId));
        } catch (EmptyResultDataAccessException e) {
            throw  new ResourceNotFoundException(ExceptionMessage.GRAPH_NOT_FOUND);
        }

    }
}
