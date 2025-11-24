package com.technicaltest.fileprocessor.person.web;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technicaltest.fileprocessor.person.service.PersonDetailsService;
import com.technicaltest.fileprocessor.shared.exception.AuthorizationValidationException;
import com.technicaltest.fileprocessor.shared.exception.ValidationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/person-details")
@RequiredArgsConstructor
public class PersonDetailsController {

    private final PersonDetailsService personDetailsService;
    private final ObjectMapper objectMapper;

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(personDetailsService.processPersonDetailsFile(file.getInputStream()));
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"OutcomeFile.json\"");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(jsonResponse.getBytes().length);
        return ResponseEntity.ok()
                .headers(headers)
                .body(jsonResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("problems", ex.getProblems());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationValidationException.class)
        public ResponseEntity<Map<String, Object>> handleAuthorizationValidationException(AuthorizationValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("problems", ex.getProblems());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}