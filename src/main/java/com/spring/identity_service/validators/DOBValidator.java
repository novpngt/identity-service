package com.spring.identity_service.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DOBValidator implements ConstraintValidator<DOBConstraint, LocalDate> {
    int min;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(value)){
            return true;
        }
        long years = ChronoUnit.YEARS.between(value, LocalDate.now());
        return years >= min;
    }

    @Override
    public void initialize(DOBConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }
}
