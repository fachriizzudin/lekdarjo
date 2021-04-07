package com.lazuardifachri.bps.lekdarjo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GraphDataValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface GraphDataConstraint {
    String message() default "Use decimal format with periods \".\" instead of commas ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}