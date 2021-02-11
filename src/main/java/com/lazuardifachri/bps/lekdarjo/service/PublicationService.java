package com.lazuardifachri.bps.lekdarjo.service;

import com.lazuardifachri.bps.lekdarjo.model.EDistrict;
import com.lazuardifachri.bps.lekdarjo.model.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

public interface PublicationService {
    Publication createPublication(String pubJson, MultipartFile file) throws IOException, ParseException;

    Page<Publication> readAllPublication(Pageable pageable);

    Publication readPublicationByid(String pubId);

    Page<Publication> readPublicationByYear(String year, Pageable pageable);

    Page<Publication> readPublicationByDistrict(EDistrict code, Pageable pageable);

    Page<Publication> readPublicationByDistrictAndYear(EDistrict code, String year, Pageable pageable);

    Page<Publication> readPublicationBySubject(String subjectId, Pageable pageable);

    Page<Publication> readPublicationBySubjectAndYear(String subjectId, String year, Pageable pageable);

    Publication updatePublication(String pubId, String pubJson, MultipartFile file) throws IOException, ParseException;

    void deletePublication(String pubId);
}
