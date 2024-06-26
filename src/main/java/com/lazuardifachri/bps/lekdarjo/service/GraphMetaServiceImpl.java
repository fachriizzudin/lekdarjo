package com.lazuardifachri.bps.lekdarjo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.admin_controller.AdmGraphController;
import com.lazuardifachri.bps.lekdarjo.exception.BadRequestException;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import com.lazuardifachri.bps.lekdarjo.model.Graph;
import com.lazuardifachri.bps.lekdarjo.model.GraphMeta;
import com.lazuardifachri.bps.lekdarjo.model.Infographic;
import com.lazuardifachri.bps.lekdarjo.repository.GraphMetaRepository;
import com.lazuardifachri.bps.lekdarjo.repository.GraphRepository;
import com.lazuardifachri.bps.lekdarjo.validation.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.time.Year;
import java.util.*;

@Transactional
@Service
public class GraphMetaServiceImpl implements GraphMetaService{

    Logger log = LoggerFactory.getLogger(GraphMetaServiceImpl.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ValidationService validationService;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    GraphMetaRepository graphMetaRepository;

    @Autowired
    GraphRepository graphRepository;

    @Override
    public GraphMeta createGraphMeta(String graphJson, MultipartFile file) throws IOException {
        GraphMeta graphMeta = objectMapper.readValue(graphJson, GraphMeta.class);

        graphMeta.setHorizontal("Tahun");
        graphMeta.setGraphType(1);

        if (file != null) {
            FileModel imageFile = fileStorageService.createFileObject(file);

            validationService.validateImageFile(imageFile);

            graphMeta.setImage(imageFile);

            validationService.validateGraphMeta(graphMeta);

            GraphMeta savedGraphMeta = graphMetaRepository.save(graphMeta);

            String imageUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/graphmeta/images/")
                    .path(String.valueOf(savedGraphMeta.getImage().getId()))
                    .toUriString();

            savedGraphMeta.setImageUri(imageUri);

            // untuk menghindari data null pada saat graph baru dibuat
            int year = Year.now().getValue();
            Graph initiateGraph = new Graph(0,1.0, year, savedGraphMeta);
            graphRepository.save(initiateGraph);

            return savedGraphMeta;
        }

        if (graphMeta.getImageUri().isEmpty()) {
            throw new BadRequestException(ExceptionMessage.FILE_OR_URI_REQUIRED);
        }

        validationService.validateGraphMeta(graphMeta);

        GraphMeta savedGraphMeta = graphMetaRepository.save(graphMeta);

        // untuk menghindari data null pada saat graph baru dibuat
        int year = Year.now().getValue();
        Graph initiateGraph = new Graph(0,1.0, year, savedGraphMeta);
        graphRepository.save(initiateGraph);

        return savedGraphMeta;
    }

    @Override
    public List<GraphMeta> readAllGraphMeta() {
        List<GraphMeta> graphMetas = graphMetaRepository.findAll();
        graphMetas.sort(Comparator.comparing(GraphMeta::getSerialNumber));
        return graphMetas;
    }

    @Override
    public List<Integer> readAllSerialNumber() {
        return graphMetaRepository.readAllSerialNumber();
    }

    @Override
    public List<Integer> readAllSerialNumber(Integer include) {
        List<Integer> options = readAllSerialNumber();
        options.add(include);
        Collections.sort(options);
        return options;
    }

    @Override
    public Long getGraphMetaCount() {
        return graphMetaRepository.count();
    }

    @Override
    public GraphMeta readGraphMetaByid(String metaId) {
        Optional<GraphMeta> graphMeta = graphMetaRepository.findBySerialNumber(Integer.parseInt(metaId));
        return graphMeta.orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.GRAPH_NOT_FOUND));
    }

    @Override
    public GraphMeta updateGraphMeta(String metaId, String metaJson, MultipartFile file) throws IOException {
        Optional<GraphMeta> graphMetaOptional = graphMetaRepository.findBySerialNumber(Integer.parseInt(metaId));
        GraphMeta newGraphMeta = objectMapper.readValue(metaJson, GraphMeta.class);
        GraphMeta graphMeta;
        if (graphMetaOptional.isPresent()) {
            graphMeta = graphMetaOptional.get();
            graphMeta.setTitle(newGraphMeta.getTitle());
            graphMeta.setSubject(newGraphMeta.getSubject());
            graphMeta.setHorizontal("Tahun");
            graphMeta.setVertical(newGraphMeta.getVertical());
            graphMeta.setVerticalUnit(newGraphMeta.getVerticalUnit());
            graphMeta.setDescription(newGraphMeta.getDescription());
            graphMeta.setGraphType(1);
            graphMeta.setDataType(newGraphMeta.getDataType());
            graphMeta.setImageUri(newGraphMeta.getImageUri());

            if (file != null) {
                FileModel imageFile;

                if (graphMeta.getImage() != null) {
                    imageFile = fileStorageService.updateFileById(String.valueOf(graphMeta.getId()), file);
                } else {
                    imageFile = fileStorageService.createFileObject(file);
                }

                validationService.validateImageFile(imageFile);
                graphMeta.setImage(imageFile);

            }

            // sepertinya tidak perlu dilakukan pada saat update karena pada update sudah
            // ada default url yang tersimpan
//            if (graphMeta.getImageUri().isEmpty()) {
//                throw new BadRequestException(ExceptionMessage.FILE_OR_URI_REQUIRED);
//            }
            
            validationService.validateGraphMeta(graphMeta);

            GraphMeta savedGraphMeta = graphMetaRepository.save(graphMeta);

            if (savedGraphMeta.getImage() != null && newGraphMeta.getImageUri() == null) {
                String imageUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/graphmeta/images/")
                        .path(String.valueOf(savedGraphMeta.getImage().getId()))
                        .toUriString();

                savedGraphMeta.setImageUri(imageUri);
            }

            return savedGraphMeta;
        } else {
            throw  new ResourceNotFoundException(ExceptionMessage.GRAPH_NOT_FOUND);
        }
    }

    @Override
    public void deleteGraphMeta(String metaId) {
        try {
            Optional<GraphMeta> graphMetaOptional = graphMetaRepository.findBySerialNumber(Integer.parseInt(metaId));
            graphMetaOptional.ifPresent(graphMeta -> graphMetaRepository.deleteById(graphMeta.getId()));
        } catch (EmptyResultDataAccessException e) {
            throw  new ResourceNotFoundException(ExceptionMessage.GRAPH_NOT_FOUND);
        }

    }
}
