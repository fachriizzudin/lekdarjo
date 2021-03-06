package com.lazuardifachri.bps.lekdarjo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.exception.BadRequestException;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import com.lazuardifachri.bps.lekdarjo.model.Indicator;
import com.lazuardifachri.bps.lekdarjo.repository.IndicatorRepository;
import com.lazuardifachri.bps.lekdarjo.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

import static com.lazuardifachri.bps.lekdarjo.Utils.parseComplicatedDate;

@Service
@Transactional
public class IndicatorServiceImpl implements IndicatorService{

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ValidationService validationService;

    @Autowired
    IndicatorRepository indicatorRepository;

    private Indicator parseIdkJsonString(String idkJson) throws JsonProcessingException, ParseException {

        Indicator indicator = objectMapper.readValue(idkJson, Indicator.class);

        indicator.setReleaseDate(parseComplicatedDate(indicator.getReleaseDate()));

        return indicator;
    }

    @Override
    public Indicator createIndicator(String idkJson, MultipartFile file) throws IOException, ParseException {

        Indicator indicator = parseIdkJsonString(idkJson);

        if (indicatorRepository.existsByTitle(indicator.getTitle())) throw new BadRequestException(ExceptionMessage.ALREADY_EXIST);

        FileModel documentFile = fileStorageService.createFileObject(file);
        validationService.validateExcelFile(documentFile);
        indicator.setDocument(documentFile);

        validationService.validateIndicator(indicator);

        Indicator savedIndicator = indicatorRepository.save(indicator);

        String documentUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/indicators/files/")
                .path(String.valueOf(savedIndicator.getDocument().getId()))
                .toUriString();

        savedIndicator.setDocumentUri(documentUri);

        return savedIndicator;
    }

    @Override
    public Page<Indicator> readAllIndicator(Pageable pageable) {
        return indicatorRepository.findAllInfo(pageable);
    }

    @Override
    public Indicator readIndicatorByid(String idkId) {
        Optional<Indicator> indicatorOptional = indicatorRepository.findByIdInfo(Long.valueOf(idkId));
        return indicatorOptional.orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.IDK_NOT_FOUND));
    }

    @Override
    public Page<Indicator> readIndicatorByCategory(String categoryId, Pageable pageable) {
        return indicatorRepository.findByCategory(Long.valueOf(categoryId), pageable);
    }

    @Override
    public Page<Indicator> readIndicatorBySubject(String subjectdId, Pageable pageable) {
        return indicatorRepository.findBySubject(Long.valueOf(subjectdId), pageable);
    }

    @Override
    public Indicator updateIndicator(String idkId, String idkJson, MultipartFile file) throws IOException, ParseException {
        Optional<Indicator> indicatorOptional = indicatorRepository.findById(Long.valueOf(idkId));
        Indicator indicator;
        if (indicatorOptional.isPresent()) {
            indicator = indicatorOptional.get();
            Indicator newIndicator = parseIdkJsonString(idkJson);
            indicator.setTitle(newIndicator.getTitle());
            indicator.setCategory(newIndicator.getCategory());
            indicator.setReleaseDate(newIndicator.getReleaseDate());
            indicator.setStatType(newIndicator.getStatType());

            if (file != null) {
                FileModel documentFile = fileStorageService.updateFileById(String.valueOf(indicator.getDocument().getId()), file);
                validationService.validateExcelFile(documentFile);
                indicator.setDocument(documentFile);
            }

            validationService.validateIndicator(indicator);

            Indicator savedIndicator = indicatorRepository.save(indicator);

            String documentUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/indicators/files/")
                    .path(String.valueOf(savedIndicator.getDocument().getId()))
                    .toUriString();

            savedIndicator.setDocumentUri(documentUri);

            return savedIndicator;

        } else {
            throw new ResourceNotFoundException(ExceptionMessage.IDK_NOT_FOUND);
        }

    }

    @Override
    public void deleteIndicator(String idkId) {
        try {
            indicatorRepository.deleteById(Long.valueOf(idkId));
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(ExceptionMessage.IDK_NOT_FOUND);
        }
    }
}
