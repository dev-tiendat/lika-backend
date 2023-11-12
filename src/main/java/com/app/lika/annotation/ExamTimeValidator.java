package com.app.lika.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExamTimeValidator implements ConstraintValidator<ValidExamTime, Long> {
    @Override
    public void initialize(ValidExamTime constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long examTimeInMs, ConstraintValidatorContext constraintValidatorContext) {
        return examTimeInMs != null && examTimeInMs > 0 && examTimeInMs < 86400000;
    }
}
