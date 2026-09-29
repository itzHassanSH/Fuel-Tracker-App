package com.fueltracker.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedRadiusValidator.class)
public @interface AllowedRadius {
    String message() default "radius must be one of 1, 5, 10, 15, 20, 25";
    // Both groups and payload irrelevant for now
    //    groups supports validation groups (e.g. "only check this on create, not update")
    //    payload lets you attach metadata for severity/categorization
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
