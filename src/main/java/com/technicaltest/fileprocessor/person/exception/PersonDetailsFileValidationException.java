package com.technicaltest.fileprocessor.person.exception;

import java.util.List;

import com.technicaltest.fileprocessor.shared.domain.Problem;
import com.technicaltest.fileprocessor.shared.exception.ValidationException;

public class PersonDetailsFileValidationException extends ValidationException {
    
    public PersonDetailsFileValidationException(String message, List<Problem> problems) {
        super(message, problems);
    }
}
