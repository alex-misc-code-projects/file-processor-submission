package com.technicaltest.fileprocessor.shared.exception;

import java.util.List;

import com.technicaltest.fileprocessor.shared.domain.Problem;

public class AuthorizationValidationException extends ValidationException {

    public AuthorizationValidationException(String message, List<Problem> problems) {
        super(message, problems);
    }
}
