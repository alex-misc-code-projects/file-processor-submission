package com.technicaltest.fileprocessor.person.transformer;

import java.util.List;

import org.springframework.stereotype.Component;

import com.technicaltest.fileprocessor.person.domain.PersonDetailsDTO;

@Component
public class PersonDetailsTransformer {

    public PersonDetailsDTO transformFileLineToPersonDetailsDTO(String line) {
        List<String> values = List.of(line.split("\\|"));
        return PersonDetailsDTO.builder()
                .uuid(values.get(0))
                .id(values.get(1))
                .name(values.get(2))
                .likes(values.get(3))
                .transport(values.get(4))
                .averageSpeed(values.get(5))
                .topSpeed(values.get(6))
                .build();
    }
}
