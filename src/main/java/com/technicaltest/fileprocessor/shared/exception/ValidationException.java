package com.technicaltest.fileprocessor.shared.exception;

import java.util.List;

import com.technicaltest.fileprocessor.shared.domain.Problem;

public class ValidationException extends FileProcessorException {

    private List<Problem> problems;
    
    public ValidationException(String message, List<Problem> problems) {
        super(message);
        this.problems = problems;
    }

    public List<Problem> getProblems() {
        return this.problems;
    }
}
