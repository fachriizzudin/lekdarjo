package com.lazuardifachri.bps.lekdarjo.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.exception.ResourceNotFoundException;
import com.lazuardifachri.bps.lekdarjo.model.Category;
import com.lazuardifachri.bps.lekdarjo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

public class CategoryDeserializer extends JsonDeserializer<Category> {

    @Autowired
    CategoryRepository repository;

    @Override
    public Category deserialize(JsonParser jp, DeserializationContext ctx) throws IOException, JsonProcessingException {

        Category category = new Category();

        String id = jp.getText();

        Optional<Category> optionalCategory = repository.findById(Long.valueOf(id));

        return optionalCategory.orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.CATEGORY_NOT_FOUND));
    }


}
