package com.technicaltest.fileprocessor.shared.exception;

public class ExternalServiceException extends FileProcessorException {
    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
