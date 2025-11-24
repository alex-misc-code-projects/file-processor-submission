package com.technicaltest.fileprocessor.shared.exception;

public class FileProcessorException extends RuntimeException {
    public FileProcessorException(String message) {
        super(message);
    }

    public FileProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
