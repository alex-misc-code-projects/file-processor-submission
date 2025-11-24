package com.technicaltest.fileprocessor.person.validation;

import com.technicaltest.fileprocessor.person.exception.PersonDetailsFileValidationException;
import com.technicaltest.fileprocessor.shared.domain.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class PersonDetailsFileValidator {
    
    private static final int EXPECTED_NUMBER_OF_FIELDS = 7;
    
    public void validateFile(List<String> lines) {
        List<Problem> allProblems = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            allProblems.addAll(validateLine(lines.get(i), i + 1));
        }
        if (!allProblems.isEmpty()) {
            throw new PersonDetailsFileValidationException("File validation failed", allProblems);
        }
    }

    private List<Problem> validateLine(String line, int lineNumber) {
        return Stream.of(
            validateLineHasCorrectNumberOfFields(line, lineNumber)
        ).flatMap(List::stream).collect(Collectors.toList());
    }

    private List<Problem> validateLineHasCorrectNumberOfFields(String line, int lineNumber) {
        int actualNumberOfFields = line.split("\\|").length;
        List<Problem> problems = new ArrayList<>();
        if (actualNumberOfFields != EXPECTED_NUMBER_OF_FIELDS) {
            problems.add(Problem.builder()
                .identifier(Integer.toString(lineNumber))
                .identifierType("Line Number")
                .description(String.format("Incorrect number of fields. Expected %s but found %s", EXPECTED_NUMBER_OF_FIELDS, actualNumberOfFields))
                .build());
        }
        return problems;
    }
}
