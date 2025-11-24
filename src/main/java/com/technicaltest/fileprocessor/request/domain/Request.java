package com.technicaltest.fileprocessor.request.domain;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request {
    
    @Id
    private UUID id;
    private String uri;
    private Instant happenedAt;
    private Integer httpResponseCode;
    private String requesterIpAddress;
    private String requesterCountry;
    private String requesterInternetServiceProvider;
    private Long millisecondsTakenToCompleteRequest;
}
