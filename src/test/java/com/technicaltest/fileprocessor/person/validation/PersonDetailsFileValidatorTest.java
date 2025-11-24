package com.technicaltest.fileprocessor.person.validation;

import com.technicaltest.fileprocessor.person.exception.PersonDetailsFileValidationException;
import com.technicaltest.fileprocessor.shared.domain.Problem;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersonDetailsFileValidatorTest {

    private final PersonDetailsFileValidator validator = new PersonDetailsFileValidator();

    @Test
    public void validateFile_validLine_noException() {
        String line = "a|b|c|d|e|f|g";
        validator.validateFile(List.of(line));
    }

    @Test
    public void validateFile_incorrectNumberOfFields_throws() {
        String line = "a|b|c|d|e";
        PersonDetailsFileValidationException ex = assertThrows(PersonDetailsFileValidationException.class, () -> validator.validateFile(List.of(line)));

        List<Problem> problems = ex.getProblems();
        assertThat(problems).isNotEmpty();

        Problem p = problems.get(0);
        assertThat(p.getIdentifier()).isEqualTo("1");
        assertThat(p.getIdentifierType()).isEqualTo("Line Number");
        assertThat(p.getDescription()).contains("Expected 7");
    }

    @Test
    public void validateFile_multipleLines_reportsCorrectLineNumber() {
        String valid = "a|b|c|d|e|f|g";
        String invalid = "1|2|3";

        PersonDetailsFileValidationException ex = assertThrows(PersonDetailsFileValidationException.class,
            () -> validator.validateFile(List.of(valid, invalid)));

        List<Problem> problems = ex.getProblems();
        assertThat(problems).hasSize(1);
        assertThat(problems.get(0).getIdentifier()).isEqualTo("2");
    }

    @Test
    public void validateFile_emptyList_noException() {
        validator.validateFile(List.of());
    }
}
