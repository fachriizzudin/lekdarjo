package com.lazuardifachri.bps.lekdarjo.service;

import com.lazuardifachri.bps.lekdarjo.model.Infographic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

public interface InfographicService {
    Infographic createInfographic(String infoJson, MultipartFile file) throws IOException, ParseException;

    Page<Infographic> readAllInfographic(Pageable pageable);

    Infographic readInfographicByid(String infoId);

    Page<Infographic> readInfographicBySubject(String subjectId, Pageable pageable);

    Infographic updateInfographic(String infoId, String infoJson, MultipartFile file) throws IOException, ParseException;

    void deleteInfographic(String pubId);
}
