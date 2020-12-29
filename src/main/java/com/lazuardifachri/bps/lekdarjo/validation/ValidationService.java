package com.lazuardifachri.bps.lekdarjo.validation;

import com.lazuardifachri.bps.lekdarjo.exception.BadRequestException;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.*;
import java.util.Set;

@Service
@Validated
public class ValidationService {

    Logger log = LoggerFactory.getLogger(ValidationService.class);

    private Validator validator;

    public ValidationService(Validator validator) {
        this.validator = validator;
    }

    public void validateStatisticalNews(StatisticalNews statisticalNews){
        Set<ConstraintViolation<StatisticalNews>> violations = validator.validate(statisticalNews);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public void validatePublication(Publication publication){
        Set<ConstraintViolation<Publication>> violations = validator.validate(publication);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public void validateInfographic(Infographic infographic){
        Set<ConstraintViolation<Infographic>> violations = validator.validate(infographic);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public void validateIndicator(Indicator indicator){
        Set<ConstraintViolation<Indicator>> violations = validator.validate(indicator);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public void validateGraphMeta(GraphMeta graphMeta){
        Set<ConstraintViolation<GraphMeta>> violations = validator.validate(graphMeta);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public void validatePdfFile(FileModel file) {
        if (file.getType().equals("application/pdf")) {
            Set<ConstraintViolation<FileModel>> violations = validator.validate(file);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        } else {
            throw new BadRequestException(ExceptionMessage.WRONG_FILE_TYPE);
        }
    }

    public void validateImageFile(FileModel file) {
        if (file.getType().equals("image/jpeg") || file.getType().equals("image/png")) {
            Set<ConstraintViolation<FileModel>> violations = validator.validate(file);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        } else {
            throw new BadRequestException(ExceptionMessage.WRONG_FILE_TYPE);
        }
    }

    public void validateExcelFile(FileModel file) {
        if (file.getType().equals("application/vnd.ms-excel")) {
            Set<ConstraintViolation<FileModel>> violations = validator.validate(file);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        } else {
            throw new BadRequestException(ExceptionMessage.WRONG_FILE_TYPE);
        }
    }

}
