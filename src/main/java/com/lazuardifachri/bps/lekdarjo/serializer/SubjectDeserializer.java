package com.lazuardifachri.bps.lekdarjo.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.Subject;
import com.lazuardifachri.bps.lekdarjo.repository.SubjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

public class SubjectDeserializer extends JsonDeserializer<Subject> {

    Logger log = LoggerFactory.getLogger(SubjectDeserializer.class);

    @Autowired
    SubjectRepository repository;

    @Override
    public Subject deserialize(JsonParser jp, DeserializationContext ctx) throws IOException, JsonProcessingException {

        String id = jp.getText();

        Optional<Subject> optionalSubject = repository.findById(Long.valueOf(id));

        return optionalSubject.orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.SUBJECT_NOT_FOUND));
    }


}
