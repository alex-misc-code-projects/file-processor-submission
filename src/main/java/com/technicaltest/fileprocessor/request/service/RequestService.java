package com.technicaltest.fileprocessor.request.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.technicaltest.fileprocessor.request.domain.Request;
import com.technicaltest.fileprocessor.request.repository.RequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestService {
    
    private final RequestRepository requestRepository;

    public List<Request> getAllRequests() {
        return StreamSupport.stream(requestRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Request saveRequest(Request request) {
        return requestRepository.save(request);
    }
}
