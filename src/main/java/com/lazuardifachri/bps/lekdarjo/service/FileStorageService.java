package com.lazuardifachri.bps.lekdarjo.service;

import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileStorageService {
    FileModel createFileObject(MultipartFile file) throws IOException;
    FileModel readFileById(String fileId);
    List<FileModel> readAllFileInfo();
    FileModel updateFileById(String fileId, MultipartFile file) throws IOException;
    FileModel createCoverFromPdf(MultipartFile file) throws IOException;
    void deleteFileById(String fileId);
}
