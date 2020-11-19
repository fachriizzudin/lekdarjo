package com.lazuardifachri.bps.lekdarjo.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {
    private List<Violation> violations = new ArrayList<>();

    public ValidationErrorResponse(List<Violation> violations) {
        this.violations = violations;
    }

    public ValidationErrorResponse() {

    }

    public List<Violation> getViolations() {
        return violations;
    }

    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }
}

