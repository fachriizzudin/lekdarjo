package com.lazuardifachri.bps.lekdarjo.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.lazuardifachri.bps.lekdarjo.validation.ValidationErrorResponse;
import com.lazuardifachri.bps.lekdarjo.validation.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.util.NoSuchElementException;

@ControllerAdvice
class ErrorHandlingControllerAdvice {

    Logger log = LoggerFactory.getLogger(ErrorHandlingControllerAdvice.class);

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        e.getConstraintViolations().forEach(violation -> error.getViolations().add(
                new Violation(violation.getPropertyPath().toString(), violation.getMessage())));
        return error;
    }

    // Exception to be thrown when validation on an argument annotated with @Valid fails
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        log.info("method argument not valid");
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getViolations().add(
                    new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return error;
    }

    @ExceptionHandler(ParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onParseException(ParseException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        Violation violation = new Violation(String.valueOf(e.getMessage()));
        error.getViolations().add(violation);
        return error;
    }

    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onJsonParseException(JsonParseException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        Violation violation = new Violation(String.valueOf(e.getMessage()));
        error.getViolations().add(violation);
        return error;
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onInvalidFormatException(InvalidFormatException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        Violation violation = new Violation(String.valueOf(e.getMessage()));
        error.getViolations().add(violation);
        return error;
    }

//    @ExceptionHandler(NullPointerException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    ValidationErrorResponse onNullPointerException(NullPointerException e) {
//        ValidationErrorResponse error = new ValidationErrorResponse();
//        Violation violation = new Violation(String.valueOf(e.getMessage()));
//        error.getViolations().add(violation);
//        return error;
//    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onBadRequestException(BadRequestException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        Violation violation = new Violation(String.valueOf(e.getMessage()));
        error.getViolations().add(violation);
        return error;
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onNoSuchElementException(NoSuchElementException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        Violation violation = new Violation(e.getCause().toString(), e.getMessage());
        error.getViolations().add(violation);
        return error;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    ValidationErrorResponse onResourceNotFoundException(ResourceNotFoundException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        Violation violation = new Violation(String.valueOf(e.getMessage()));
        error.getViolations().add(violation);
        return error;
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    ValidationErrorResponse onEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        Violation violation = new Violation(e.getMessage());
        error.getViolations().add(violation);
        return error;
    }

    @ExceptionHandler(MismatchedInputException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    ValidationErrorResponse onMismatchedInputException(MismatchedInputException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        Violation violation = new Violation(e.getMessage());
        error.getViolations().add(violation);
        return error;
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    ValidationErrorResponse onNumberFormatException(NumberFormatException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        Violation violation = new Violation(e.getMessage());
        error.getViolations().add(violation);
        return error;
    }

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConversionFailedException(ConversionFailedException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        Violation violation = new Violation(e.getMessage());
        error.getViolations().add(violation);
        return error;
    }

}