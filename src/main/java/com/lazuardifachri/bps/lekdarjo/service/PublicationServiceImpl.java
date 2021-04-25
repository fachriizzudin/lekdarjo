package com.lazuardifachri.bps.lekdarjo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.Utils;
import com.lazuardifachri.bps.lekdarjo.exception.BadRequestException;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.EDistrict;
import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import com.lazuardifachri.bps.lekdarjo.model.Publication;
import com.lazuardifachri.bps.lekdarjo.repository.FileStorageRepository;
import com.lazuardifachri.bps.lekdarjo.repository.PublicationRepository;
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
import java.util.Optional;

import static com.lazuardifachri.bps.lekdarjo.Utils.parseComplicatedDate;

@Transactional
@Service
public class PublicationServiceImpl implements PublicationService{

    Logger log = LoggerFactory.getLogger(PublicationServiceImpl.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ValidationService validationService;

    @Autowired
    PublicationRepository publicationRepository;

    private Publication parsePubJsonString(String pubJson) throws JsonProcessingException, ParseException {

        Publication publication = objectMapper.readValue(pubJson, Publication.class);

        publication.setReleaseDate(parseComplicatedDate(publication.getReleaseDate()));

        return publication;
    }

    @Override
    public Publication createPublication(String pubJson, MultipartFile file) throws IOException, ParseException {

        Publication publication = parsePubJsonString(pubJson);

        if (publicationRepository.existsByTitle(publication.getTitle())) throw new BadRequestException(ExceptionMessage.ALREADY_EXIST);

        if (file != null) {
            FileModel documentFile = fileStorageService.createFileObject(file);
            validationService.validatePdfFile(documentFile);
            publication.setDocument(documentFile);

            FileModel imageFile = fileStorageService.createCoverFromPdf(file);
            publication.setImage(imageFile);


            validationService.validatePublication(publication);

            Publication savedPublication = publicationRepository.save(publication);

            // set property setelah save ke database untuk mendapatkan id dari file dan cover

            String documentUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/publications/files/")
                    .path(String.valueOf(savedPublication.getDocument().getId()))
                    .toUriString();

            String imageUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/publications/images/")
                    .path(String.valueOf(savedPublication.getImage().getId()))
                    .toUriString();

            publication.setDocumentUri(documentUri);
            publication.setImageUri(imageUri);

            return savedPublication;
        }

        if (publication.getDocumentUri().isEmpty() || publication.getImageUri().isEmpty()) {
            throw new BadRequestException(ExceptionMessage.FILE_OR_URI_REQUIRED);
        }

        publication.setDocumentUri(Utils.formatUrlFromBps(publication.getDocumentUri()));

        validationService.validatePublication(publication);

        return publicationRepository.save(publication);
    }

    @Override
    public Page<Publication> readAllPublication(Pageable pageable) {
        return publicationRepository.findAllInfo(pageable);
    }

    @Override
    public Publication readPublicationByid(String pubId) {
        Optional<Publication> publication = publicationRepository.findByIdInfo(Long.parseLong(pubId));
        return publication.orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.NEWS_NOT_FOUND));
    }

    @Override
    public Page<Publication> readPublicationByDistrict(EDistrict code, Pageable pageable) {
        return publicationRepository.findByDistrict(code, pageable);
    }

    @Override
    public Page<Publication> readPublicationByYear(String year, Pageable pageable) {
        return publicationRepository.findByYear(Integer.valueOf(year), pageable);
    }

    @Override
    public Page<Publication> readPublicationByDistrictAndYear(EDistrict code, String year, Pageable pageable) {
        return publicationRepository.findByDistrictAndYear(code, Integer.valueOf(year), pageable);
    }

    @Override
    public Page<Publication> readPublicationBySubject(String subjectId, Pageable pageable) {
        return publicationRepository.findBySubject(Long.valueOf(subjectId), pageable);
    }

    @Override
    public Page<Publication> readPublicationBySubjectAndYear(String subjectId, String year, Pageable pageable) {
        return publicationRepository.findBySubjectAndYear(Long.valueOf(subjectId), Integer.valueOf(year), pageable);
    }

    @Override
    public Publication updatePublication(String pubId, String pubJson, MultipartFile file) throws IOException, ParseException {
        Optional<Publication> publicationOptional = publicationRepository.findById(Long.valueOf(pubId));
        Publication publication;
        if (publicationOptional.isPresent()) {
            publication = publicationOptional.get();
            Publication newPublication = parsePubJsonString(pubJson);
            publication.setTitle(newPublication.getTitle());
            publication.setCatalogNo(newPublication.getCatalogNo());
            publication.setPublicationNo(newPublication.getPublicationNo());
            publication.setIssnOrIsbn(newPublication.getIssnOrIsbn());
            publication.setReleaseDate(parseComplicatedDate(newPublication.getReleaseDate()));
            publication.setInformation(newPublication.getInformation());
            publication.setSubject(newPublication.getSubject());
            publication.setDistrict(newPublication.getDistrict());
            // publication.setDocumentUri(newPublication.getDocumentUri());
            publication.setDocumentUri(Utils.formatUrlFromBps(publication.getDocumentUri()));
            publication.setImageUri(newPublication.getImageUri());

            if (file != null) {

                FileModel documentFile;

                if (publication.getDocument() != null) {
                    documentFile = fileStorageService.updateFileById(String.valueOf(publication.getDocument().getId()), file);
                } else {
                    documentFile = fileStorageService.createFileObject(file);
                }

                validationService.validatePdfFile(documentFile);
                publication.setDocument(documentFile);

                FileModel imageFile = fileStorageService.createCoverFromPdf(file);
                publication.setImage(imageFile);
            }

            validationService.validatePublication(publication);

            Publication savedPublication = publicationRepository.save(publication);

            // set property setelah save ke database untuk mendapatkan id dari file dan cover

            if (savedPublication.getDocument() != null && newPublication.getImageUri() == null && newPublication.getDocumentUri() == null) {
                String documentUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/publications/files/")
                        .path(String.valueOf(savedPublication.getDocument().getId()))
                        .toUriString();

                String imageUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/publications/images/")
                        .path(String.valueOf(savedPublication.getImage().getId()))
                        .toUriString();

                publication.setDocumentUri(documentUri);
                publication.setImageUri(imageUri);
            }

            return savedPublication;
        } else {
            throw new ResourceNotFoundException(ExceptionMessage.PUB_NOT_FOUND);
        }

    }

    @Override
    public void deletePublication(String pubId) {
        try {
            publicationRepository.deleteById(Long.valueOf(pubId));
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(ExceptionMessage.PUB_NOT_FOUND);
        }
    }
}
