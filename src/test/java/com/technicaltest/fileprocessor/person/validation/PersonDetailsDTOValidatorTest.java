package com.technicaltest.fileprocessor.person.validation;

import com.technicaltest.fileprocessor.person.domain.PersonDetailsDTO;
import com.technicaltest.fileprocessor.person.exception.PersonDetailsFileValidationException;
import com.technicaltest.fileprocessor.shared.domain.Problem;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.technicaltest.fileprocessor.person.fixture.PersonDetailsDTOFixture.givenPersonDetailsDTOBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersonDetailsDTOValidatorTest {

    private final PersonDetailsDTOValidator validator = new PersonDetailsDTOValidator();

    @Test
    public void validatePersonDetailsDTOs_validDTO_noException() {
        PersonDetailsDTO dto = givenValidPersonDetailsDTO()
                .build();

        validator.validatePersonDetailsDTOs(List.of(dto));
    }

    @Test
    public void validatePersonDetailsDTOs_invalidUuid_throwsWithLineNumber() {
        PersonDetailsDTO valid = givenValidPersonDetailsDTO()
                .build();

        PersonDetailsDTO invalid = givenPersonDetailsDTOBuilder()
                .uuid("not-a-uuid")
                .build();

        PersonDetailsFileValidationException ex = assertThrows(PersonDetailsFileValidationException.class,
            () -> validator.validatePersonDetailsDTOs(List.of(valid, invalid)));

        List<Problem> problems = ex.getProblems();
        assertThat(problems).isNotEmpty();
        assertThat(problems.get(0).getIdentifier()).isEqualTo("2");
    }

    private PersonDetailsDTO.PersonDetailsDTOBuilder givenValidPersonDetailsDTO() {
        return givenPersonDetailsDTOBuilder();
    }
}
