package com.lazuardifachri.bps.lekdarjo.service;

import com.lazuardifachri.bps.lekdarjo.model.Graph;
import com.lazuardifachri.bps.lekdarjo.model.Graph;
import com.lazuardifachri.bps.lekdarjo.model.GraphMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface GraphService {

    List<Graph> createGraphBulk(String graphJson, String metaId) throws IOException, ParseException;

    Graph createGraph(String graphJson, String metaId) throws IOException, ParseException;

    List<Graph> readAllGraph(String metaId);

    Graph readById(String dataId);

    Graph updateGraph(String dataId, String dataJson, String metaId) throws IOException, ParseException;

    void deleteGraph(String dataId);
}
