package com.technicaltest.fileprocessor.ip.exception;

import java.util.List;

import com.technicaltest.fileprocessor.shared.domain.Problem;
import com.technicaltest.fileprocessor.shared.exception.AuthorizationValidationException;

public class IPAddressAuthorizationValidationException extends AuthorizationValidationException {

    public IPAddressAuthorizationValidationException(String message, List<Problem> problems) {
        super(message, problems);
    }
}
