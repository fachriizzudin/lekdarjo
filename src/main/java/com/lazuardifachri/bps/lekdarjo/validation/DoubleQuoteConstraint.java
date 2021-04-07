package com.lazuardifachri.bps.lekdarjo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DoubleQuoteValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleQuoteConstraint {
    String message() default "Text cannot contain double quotes";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}