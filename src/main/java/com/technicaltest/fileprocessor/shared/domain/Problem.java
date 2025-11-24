package com.technicaltest.fileprocessor.shared.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Problem {
    
    private String description;
    private String identifierType;
    private String identifier;
}
