package com.lazuardifachri.bps.lekdarjo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DoubleQuoteValidator implements
        ConstraintValidator<DoubleQuoteConstraint, String> {

    @Override
    public void initialize(DoubleQuoteConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext context) {
        return field != null && !field.contains("\"");
    }
}
