package com.lazuardifachri.bps.lekdarjo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.exception.BadRequestException;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import com.lazuardifachri.bps.lekdarjo.model.Infographic;
import com.lazuardifachri.bps.lekdarjo.repository.InfographicRepository;
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
public class InfographicServiceImpl implements InfographicService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ValidationService validationService;

    @Autowired
    InfographicRepository infographicRepository;

    private Infographic parseInfoJsonString(String infoJson) throws JsonProcessingException, ParseException {

        Infographic infographic = objectMapper.readValue(infoJson, Infographic.class);

        infographic.setReleaseDate(parseComplicatedDate(infographic.getReleaseDate()));

        return infographic;
    }

    @Override
    public Infographic createInfographic(String infoJson, MultipartFile file) throws IOException, ParseException {

        Infographic infographic = parseInfoJsonString(infoJson);

        if (infographicRepository.existsByTitle(infographic.getTitle())) throw new BadRequestException(ExceptionMessage.ALREADY_EXIST);

        if (file != null) {
            FileModel imageFile = fileStorageService.createFileObject(file);

            validationService.validateImageFile(imageFile);

            infographic.setImage(imageFile);

            validationService.validateInfographic(infographic);

            Infographic savedInfographic = infographicRepository.save(infographic);

            String imageUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/infographics/images/")
                    .path(String.valueOf(savedInfographic.getImage().getId()))
                    .toUriString();

            savedInfographic.setImageUri(imageUri);

            return savedInfographic;
        }

        validationService.validateInfographic(infographic);

        Infographic savedInfographic = infographicRepository.save(infographic);

        return savedInfographic;

    }

    @Override
    public Page<Infographic> readAllInfographic(Pageable pageable) {
        return infographicRepository.findAllInfo(pageable);
    }

    @Override
    public Infographic readInfographicByid(String infoId) {
        Optional<Infographic> infographicOptional = infographicRepository.findByIdInfo(Long.valueOf(infoId));
        return infographicOptional.orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.INFOGRAPHIC_NOT_FOUND));
    }

    @Override
    public Page<Infographic> readInfographicBySubject(String subjectId, Pageable pageable) {
        return infographicRepository.findBySubject(Long.valueOf(subjectId), pageable);
    }

    @Override
    public Infographic updateInfographic(String infoId, String infoJson, MultipartFile file) throws IOException, ParseException {

        Optional<Infographic> infographicOptional = infographicRepository.findById(Long.valueOf(infoId));
        Infographic infographic;

        if (infographicOptional.isPresent()) {
            infographic = infographicOptional.get();
            Infographic newInfographic = parseInfoJsonString(infoJson);
            infographic.setTitle(newInfographic.getTitle());
            infographic.setReleaseDate(newInfographic.getReleaseDate());
            infographic.setSubject(newInfographic.getSubject());
            infographic.setImageUri(newInfographic.getImageUri());

            if (file != null) {

                FileModel imageFile;

                if (infographic.getImage() != null) {
                    imageFile = fileStorageService.updateFileById(String.valueOf(infographic.getImage().getId()), file);
                } else {
                    imageFile = fileStorageService.createFileObject(file);
                }

                validationService.validateImageFile(imageFile);
                infographic.setImage(imageFile);
            }

            validationService.validateInfographic(infographic);

            Infographic savedInfographic = infographicRepository.save(infographic);

            if (savedInfographic.getImage() != null && newInfographic.getImageUri() == null) {
                String imageUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/infographics/images/")
                        .path(String.valueOf(savedInfographic.getImage().getId()))
                        .toUriString();

                savedInfographic.setImageUri(imageUri);
            }

            return savedInfographic;

        } else {
            throw new ResourceNotFoundException(ExceptionMessage.INFOGRAPHIC_NOT_FOUND);
        }
    }

    @Override
    public void deleteInfographic(String pubId) {
        try {
            infographicRepository.deleteById(Long.valueOf(pubId));
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(ExceptionMessage.INFOGRAPHIC_NOT_FOUND);
        }
    }
}
