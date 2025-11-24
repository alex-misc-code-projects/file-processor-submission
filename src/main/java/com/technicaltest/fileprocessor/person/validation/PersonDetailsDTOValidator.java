package com.technicaltest.fileprocessor.person.validation;

import static com.technicaltest.fileprocessor.shared.validation.ValidationUtils.validateFieldIsNotEmpty;
import static com.technicaltest.fileprocessor.shared.validation.ValidationUtils.validateFieldIsNumericAndNotNegative;
import static com.technicaltest.fileprocessor.shared.validation.ValidationUtils.validateFieldIsValidUUID;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.technicaltest.fileprocessor.person.domain.PersonDetailsDTO;
import com.technicaltest.fileprocessor.person.exception.PersonDetailsFileValidationException;
import com.technicaltest.fileprocessor.shared.domain.Problem;

@Component
public class PersonDetailsDTOValidator {
 
    public void validatePersonDetailsDTOs(List<PersonDetailsDTO> personDetailsDTOs) {
        List<Problem> allProblems = new ArrayList<>();
        for (int i = 0; i < personDetailsDTOs.size(); i++) {
            allProblems.addAll(validatePersonDetailsDTO(personDetailsDTOs.get(i), i + 1));
        }
        if (!allProblems.isEmpty()) {
            throw new PersonDetailsFileValidationException("PersonDetails validation failed", allProblems);
        }
    }

    private List<Problem> validatePersonDetailsDTO(PersonDetailsDTO personDetailsDTO, Integer lineNumber) {
        List<Problem> problems = Stream.of(
            validateUUID(personDetailsDTO),
            validateId(personDetailsDTO),
            validateName(personDetailsDTO),
            validateLikes(personDetailsDTO),
            validateTransport(personDetailsDTO),
            validateAverageSpeed(personDetailsDTO),
            validateTopSpeed(personDetailsDTO)
        ).flatMap(List::stream).collect(Collectors.toList());
        problems.forEach(problem -> {
            problem.setIdentifier(Integer.toString(lineNumber));
            problem.setIdentifierType("Line Number");
        });
        return problems;
    }

    private List<Problem> validateUUID(PersonDetailsDTO personDetailsDTO) {
        return validateFieldIsValidUUID(personDetailsDTO.getUuid(), "UUID");
    }

    private List<Problem> validateId(PersonDetailsDTO personDetailsDTO) {
        return validateFieldIsNotEmpty(personDetailsDTO.getId(), "ID");
    }

    private List<Problem> validateName(PersonDetailsDTO personDetailsDTO) {
        return validateFieldIsNotEmpty(personDetailsDTO.getName(), "Name");
    }

    private List<Problem> validateLikes(PersonDetailsDTO personDetailsDTO) {
        return validateFieldIsNotEmpty(personDetailsDTO.getLikes(), "Likes");
    }

    private List<Problem> validateTransport(PersonDetailsDTO personDetailsDTO) {
        return validateFieldIsNotEmpty(personDetailsDTO.getTransport(), "Transport");
    }

    private List<Problem> validateAverageSpeed(PersonDetailsDTO personDetailsDTO) {
        return validateFieldIsNumericAndNotNegative(personDetailsDTO.getAverageSpeed(), "Average Speed");
    }

    private List<Problem> validateTopSpeed(PersonDetailsDTO personDetailsDTO) {
        return validateFieldIsNumericAndNotNegative(personDetailsDTO.getTopSpeed(), "Top Speed");
    }
}
