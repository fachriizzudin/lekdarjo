package com.lazuardifachri.bps.lekdarjo.service;

import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import com.lazuardifachri.bps.lekdarjo.repository.FileStorageRepository;
import com.lazuardifachri.bps.lekdarjo.validation.ValidationService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FileStorageServiceImpl implements FileStorageService {

    Logger log = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    @Autowired
    FileStorageRepository fileStorageRepository;

    @Autowired
    ValidationService validationService;

    @Override
    public FileModel createFileObject(MultipartFile file) throws IOException {
        return new FileModel(file.getOriginalFilename(), file.getContentType(), file.getSize(), file.getBytes());
    }

    @Override
    public FileModel readFileById(String fileId) {
        if (fileId.isEmpty()) throw new RuntimeException(ExceptionMessage.MISSING_FIELD);
        Optional<FileModel> fileDocument = fileStorageRepository.findById(Long.parseLong(fileId));
        return fileDocument.orElseThrow(() -> new RuntimeException(ExceptionMessage.FILE_NOT_FOUND));
    }

    @Override
    public List<FileModel> readAllFileInfo() {
        return fileStorageRepository.findAllInfo();
    }

    @Override
    public FileModel updateFileById(String fileId, MultipartFile file) throws IOException {
        FileModel fileModel = readFileById(fileId);
        fileModel.setName(file.getOriginalFilename());
        fileModel.setType(file.getContentType());
        fileModel.setSize(file.getSize());
        fileModel.setBytes(file.getBytes());

        return fileModel;
    }

    private File convertToFile(MultipartFile file) throws IOException {

        File convFile = null;
        Optional<String> fileName = Optional.ofNullable(file.getOriginalFilename());

        if (fileName.isPresent()) {
            String uploadDir = "\\uploads\\";
            Path path = Paths.get(System.getProperty("user.dir") + uploadDir);
            Files.createDirectories(path);
            convFile = new File(path + "\\" + file.getOriginalFilename());
            if (convFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(convFile);
                fos.write(file.getBytes());
                fos.close();
            }
        }
        return convFile;
    }

    private MultipartFile convertToMultipartFileImage(String name, String originalName, byte[] bytes) throws IOException {
        return new MockMultipartFile(name,
                originalName,
                "image/jpeg", bytes);
    }

    @Override
    public FileModel createCoverFromPdf(MultipartFile file) throws IOException {

        PDDocument document = PDDocument.load(file.getBytes());

        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImageWithDPI(0, 300, ImageType.RGB);


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", out);
        byte[] bytes = out.toByteArray();
        document.close();
        MultipartFile multipartFileImage = convertToMultipartFileImage(file.getName(), file.getOriginalFilename(), bytes);


        FileModel fileModel = new FileModel(multipartFileImage.getOriginalFilename(), "image/jpeg", multipartFileImage.getSize(), multipartFileImage.getBytes());


        return fileModel;

    }

    @Override
    public void deleteFileById(String fileId) {
        fileStorageRepository.deleteById(Long.parseLong(fileId));
    }

}
