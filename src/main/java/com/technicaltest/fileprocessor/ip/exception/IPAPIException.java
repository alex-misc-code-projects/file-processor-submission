package com.technicaltest.fileprocessor.ip.exception;

import com.technicaltest.fileprocessor.shared.exception.ExternalServiceException;

public class IPAPIException extends ExternalServiceException {
    public IPAPIException(String message) {
        super(message);
    }

    public IPAPIException(String message, Throwable cause) {
        super(message, cause);
    }
    
}