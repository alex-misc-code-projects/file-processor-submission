package com.technicaltest.fileprocessor.person.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDetailsDTO {

    private String uuid;
    private String id;
    private String name;
    private String likes;
    private String transport;
    private String averageSpeed;
    private String topSpeed;
}