package com.technicaltest.fileprocessor.shared.validation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.technicaltest.fileprocessor.shared.domain.Problem;

public class ValidationUtils {
    
    public static List<Problem> validateFieldIsValidUUID(String value, String fieldName) {
        List<Problem> problems = new ArrayList<>();
        problems.addAll(validateFieldIsNotEmpty(value, fieldName));
        if (!problems.isEmpty()) {
            return problems;
        }
        if (!isValidUUID(value)) {
            problems.add(Problem.builder()
                .description(String.format("%s is not a valid UUID", fieldName))
                .build());
        }
        return problems;
    }

    public static List<Problem> validateFieldIsNumericAndNotNegative(String value, String fieldName) {
        List<Problem> problems = new ArrayList<>();
        problems.addAll(validateFieldIsNumeric(value, fieldName));
        if (!problems.isEmpty()) {
            return problems;
        }
        BigDecimal numericValue = new BigDecimal(value);
        if (numericValue.compareTo(BigDecimal.ZERO) < 0) {
            problems.add(Problem.builder()
                .description(String.format("%s cannot be negative", fieldName))
                .build());
        }
        return problems;
    }

    public static List<Problem> validateFieldIsNumeric(String value, String fieldName) {
        List<Problem> problems = new ArrayList<>();
        problems.addAll(validateFieldIsNotEmpty(value, fieldName));
        if (!problems.isEmpty()) {
            return problems;
        }
        try {
            new BigDecimal(value);
        } catch (NumberFormatException e) {
            problems.add(Problem.builder()
                .description(String.format("%s is not a valid number", fieldName))
                .build());
        }
        return problems;
    }

    public static List<Problem> validateFieldIsNotEmpty(String value, String fieldName) {
        List<Problem> problems = new ArrayList<>();
        if (!containsValue(value)) {
            problems.add(Problem.builder()
                .description(String.format("%s is missing", fieldName))
                .build());
        }
        return problems;
    }

    private static boolean containsValue(String value) {
        return value != null && !value.isEmpty();
    }

    private static boolean isValidUUID(String uuid) {
        return uuid.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    }
}
