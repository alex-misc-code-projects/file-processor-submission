package com.technicaltest.fileprocessor.person.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDetails {
    private String name;
    private String transport;
    private BigDecimal topSpeed;
}