package com.lazuardifachri.bps.lekdarjo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.exception.BadRequestException;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import com.lazuardifachri.bps.lekdarjo.model.StatisticalNews;
import com.lazuardifachri.bps.lekdarjo.repository.CategoryRepository;
import com.lazuardifachri.bps.lekdarjo.repository.StatisticalNewsRepository;
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

@Service
@Transactional
public class StatisticalNewsServiceImpl implements StatisticalNewsService {

    Logger log = LoggerFactory.getLogger(StatisticalNewsServiceImpl.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    StatisticalNewsRepository statisticalNewsRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ValidationService validationService;


    private StatisticalNews parseNewsJsonString(String newsJson) throws JsonProcessingException, ParseException {

        StatisticalNews statisticalNews = objectMapper.readValue(newsJson, StatisticalNews.class);

        statisticalNews.setReleaseDate(parseComplicatedDate(statisticalNews.getReleaseDate()));

        return statisticalNews;
    }

    @Override
    public StatisticalNews createStatisticalNews(String newsJson, MultipartFile file) throws IOException, ParseException {

        // workaround untuk data paragraph pada field abstraksi
        objectMapper.configure(
                JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(),
                true
        );

        StatisticalNews statisticalNews = parseNewsJsonString(newsJson);

        if (statisticalNewsRepository.existsByTitle(statisticalNews.getTitle())) throw new BadRequestException(ExceptionMessage.ALREADY_EXIST);

        if (file != null) {
            FileModel documentFile = fileStorageService.createFileObject(file);

            validationService.validatePdfFile(documentFile);

            statisticalNews.setDocument(documentFile);

            validationService.validateStatisticalNews(statisticalNews);

            StatisticalNews savedStatisticalNews = statisticalNewsRepository.save(statisticalNews);

            String documentUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/statnews/files/")
                    .path(String.valueOf(savedStatisticalNews.getDocument().getId()))
                    .toUriString();

            // TERNYATA DOCUMENT URINYA BISA KESAVE WALAUPUN DATA SUDAH DISAVE SEBELUMNYA

            savedStatisticalNews.setDocumentUri(documentUri);

            return savedStatisticalNews;
        }

        validationService.validateStatisticalNews(statisticalNews);

        StatisticalNews savedStatisticalNews = statisticalNewsRepository.save(statisticalNews);

        return savedStatisticalNews;

    }

    @Override
    public Page<StatisticalNews> readAllStatisticalNews(Pageable pageable) {
        return statisticalNewsRepository.findAllInfo(pageable);
    }

    @Override
    public StatisticalNews readStatisticalNewsById(String newsId) {
        Optional<StatisticalNews> statisticalNews = statisticalNewsRepository.findByIdInfo(Long.parseLong(newsId));
        return statisticalNews.orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.NEWS_NOT_FOUND));
    }

    @Override
    public Page<StatisticalNews> readStatisticalNewsByCategory(String categoryId, Pageable pageable) {
        return statisticalNewsRepository.findByCategory(Long.valueOf(categoryId), pageable);
    }

    @Override
    public Page<StatisticalNews> readStatisticalNewsByCategoryAndYear(String categoryId, String year, Pageable pageable) {
        return statisticalNewsRepository.findByCategoryAndYear(Long.valueOf(categoryId), Integer.valueOf(year), pageable);
    }

    @Override
    public Page<StatisticalNews> readStatisticalNewsByCategoryAndYearAndMonth(String categoryId, String year, String month, Pageable pageable) {
        return statisticalNewsRepository.findByCategoryAndYearAndMonth(Long.valueOf(categoryId), Integer.valueOf(year), Integer.valueOf(month), pageable);
    }

    @Override
    public Page<StatisticalNews> readStatisticalNewsByYear(String year, Pageable pageable) {
        return statisticalNewsRepository.findByYear(Integer.valueOf(year), pageable);
    }

    @Override
    public Page<StatisticalNews> readStatisticalNewsByYearAndMonth(String year, String month, Pageable pageable) {
        return statisticalNewsRepository.findByYearAndMonth(Integer.valueOf(year), Integer.valueOf(month), pageable);
    }

    @Override
    public StatisticalNews updateStatisticalNews(String newsId, String newsJson, MultipartFile file) throws IOException, ParseException {

        Optional<StatisticalNews> statisticalNewsOptional = statisticalNewsRepository.findById(Long.valueOf(newsId));

        StatisticalNews statisticalNews;
        if (statisticalNewsOptional.isPresent()) {
            statisticalNews = statisticalNewsOptional.get();
            StatisticalNews newStatisticalNews = objectMapper.readValue(newsJson, StatisticalNews.class);
            statisticalNews.setTitle(newStatisticalNews.getTitle());
            statisticalNews.setReleaseDate(parseComplicatedDate(newStatisticalNews.getReleaseDate()));
            statisticalNews.setCategory(newStatisticalNews.getCategory());
            statisticalNews.setAbstraction(newStatisticalNews.getAbstraction());
            statisticalNews.setDocumentUri(newStatisticalNews.getDocumentUri());

            if (file != null) {
                FileModel documentFile;

                if (statisticalNews.getDocument() != null) {
                    documentFile = fileStorageService.updateFileById(String.valueOf(statisticalNews.getDocument().getId()), file);
                } else {
                    documentFile = fileStorageService.createFileObject(file);
                }

                validationService.validatePdfFile(documentFile);
                statisticalNews.setDocument(documentFile);
            }

            validationService.validateStatisticalNews(statisticalNews);

            StatisticalNews savedStatisticalNews = statisticalNewsRepository.save(statisticalNews);

            if (savedStatisticalNews.getDocument() != null && statisticalNews.getDocumentUri() == null) {
                log.info("masuk tah?");
                String documentUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/statnews/files/")
                        .path(String.valueOf(savedStatisticalNews.getDocument().getId()))
                        .toUriString();

                savedStatisticalNews.setDocumentUri(documentUri);
            }

            return savedStatisticalNews;

        } else {
            throw new ResourceNotFoundException(ExceptionMessage.NEWS_NOT_FOUND);
        }

    }

    @Override
    public void deleteStatisticalNews(String newsId) {
        try {
            statisticalNewsRepository.deleteById(Long.parseLong(newsId));
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(ExceptionMessage.NEWS_NOT_FOUND);
        }

    }
}
