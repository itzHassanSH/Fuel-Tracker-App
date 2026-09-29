package com.fueltracker.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class AllowedRadiusValidator implements ConstraintValidator<AllowedRadius, Integer> {
    private static final Set<Integer> ALLOWED = Set.of(1, 5, 10, 15, 20, 25);

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext ctx) {
        return value == null || ALLOWED.contains(value);  // Null check is done with separate annotation
    }
}
