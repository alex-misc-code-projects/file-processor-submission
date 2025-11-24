package com.technicaltest.fileprocessor.request.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.technicaltest.fileprocessor.request.domain.Request;

public interface RequestRepository extends CrudRepository<Request, UUID> {
    
}
