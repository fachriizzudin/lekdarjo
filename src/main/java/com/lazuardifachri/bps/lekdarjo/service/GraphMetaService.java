package com.lazuardifachri.bps.lekdarjo.service;

import com.lazuardifachri.bps.lekdarjo.model.GraphMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface GraphMetaService {
    GraphMeta createGraphMeta(String graphJson, MultipartFile file) throws IOException, ParseException;

    List<GraphMeta> readAllGraphMeta();

    List<Integer> readAllSerialNumber();

    GraphMeta readGraphMetaByid(String metaId);

    GraphMeta updateGraphMeta(String metaId, String metaJson, MultipartFile file) throws IOException, ParseException;

    void deleteGraphMeta(String metaId);
}
