package com.lazuardifachri.bps.lekdarjo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.Utils;
import com.lazuardifachri.bps.lekdarjo.exception.BadRequestException;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import com.lazuardifachri.bps.lekdarjo.model.Infographic;
import com.lazuardifachri.bps.lekdarjo.repository.InfographicRepository;
import com.lazuardifachri.bps.lekdarjo.validation.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Optional;

import static com.lazuardifachri.bps.lekdarjo.Utils.parseComplicatedDate;

@Service
@Transactional
public class InfographicServiceImpl implements InfographicService {

    Logger log = LoggerFactory.getLogger(InfographicServiceImpl.class);

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

            FileModel imageFile = resizeImage(file);

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

        if (infographic.getImageUri().isEmpty()) {
            throw new BadRequestException(ExceptionMessage.FILE_OR_URI_REQUIRED);
        }

        infographic.setImageUri(Utils.formatUrlFromBps(infographic.getImageUri()));

        validationService.validateInfographic(infographic);

        return infographicRepository.save(infographic);

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
            // infographic.setImageUri(newInfographic.getImageUri());
            infographic.setImageUri(Utils.formatUrlFromBps(infographic.getImageUri()));

            if (file != null) {

                FileModel imageFile;

                if (infographic.getImage() != null) {
                    imageFile = fileStorageService.updateFileById(String.valueOf(infographic.getImage().getId()), file);
                } else {
                    //imageFile = fileStorageService.createFileObject(file);
                    imageFile = resizeImage(file);
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

    private FileModel resizeImage(MultipartFile file) throws IOException {
        InputStream is = new ByteArrayInputStream(file.getBytes());
        BufferedImage resizeMe = ImageIO.read(is);

        if (resizeMe.getHeight() > 1000) {

            Dimension imgSize = new Dimension(resizeMe.getWidth(), resizeMe.getHeight());
            Dimension boundary = new Dimension(800, 800);
            Dimension scaledDimension = getScaledDimension(imgSize, boundary);

            Image resultingImage = resizeMe.getScaledInstance((int) scaledDimension.getWidth(), (int) scaledDimension.getHeight(), Image.SCALE_SMOOTH);
            BufferedImage outputImage = new BufferedImage((int) scaledDimension.getWidth(), (int) scaledDimension.getHeight(), BufferedImage.TYPE_INT_ARGB);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(outputImage, "png", baos);
            byte[] bytes = baos.toByteArray();

            return new FileModel(file.getOriginalFilename(), file.getContentType(), file.getSize(), bytes);
        }
        return new FileModel(file.getOriginalFilename(), file.getContentType(), file.getSize(), file.getBytes());
    }

    Dimension getScaledDimension(Dimension imageSize, Dimension boundary) {

        double widthRatio = boundary.getWidth() / imageSize.getWidth();
        double heightRatio = boundary.getHeight() / imageSize.getHeight();
        double ratio = Math.min(widthRatio, heightRatio);

        return new Dimension((int) (imageSize.width  * ratio),
                (int) (imageSize.height * ratio));
    }
}
