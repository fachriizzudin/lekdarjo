package com.lazuardifachri.bps.lekdarjo.service;

import com.lazuardifachri.bps.lekdarjo.model.GraphMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

public interface GraphMetaService {
    GraphMeta createGraphMeta(String graphJson) throws IOException, ParseException;

    Page<GraphMeta> readAllGraphMeta(Pageable pageable);

    GraphMeta readGraphMetaByid(String metaId);

    GraphMeta updateGraphMeta(String metaId, String metaJson) throws IOException, ParseException;

    void deleteGraphMeta(String metaId);
}
