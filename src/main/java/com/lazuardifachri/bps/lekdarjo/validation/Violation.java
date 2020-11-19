package com.lazuardifachri.bps.lekdarjo.validation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Violation {

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = "field_name")
    private String fieldName;

    @JsonProperty(value = "message")
    private String message;

    public Violation(String message) {
        this.message = message;
    }

    public Violation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
