package com.lazuardifachri.bps.lekdarjo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GraphDataValidator implements
        ConstraintValidator<GraphDataConstraint, Double> {

    @Override
    public void initialize(GraphDataConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(Double field, ConstraintValidatorContext context) {
        return field != null && !field.toString().contains(",");
    }
}
