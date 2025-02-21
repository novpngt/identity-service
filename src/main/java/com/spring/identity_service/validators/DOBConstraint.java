package com.spring.identity_service.validators;

import jakarta.validation.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DOBValidator.class)
public @interface DOBConstraint {
    String message() default "{javax.validation.constraints.DOB}";
    int min();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
