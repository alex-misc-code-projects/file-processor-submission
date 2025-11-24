package com.technicaltest.fileprocessor.person.fixture;

import com.technicaltest.fileprocessor.person.domain.PersonDetailsDTO;

public class PersonDetailsDTOFixture {

    public static PersonDetailsDTO givenPersonDetailsDTO() {
        return givenPersonDetailsDTOBuilder().build();
    }

    public static PersonDetailsDTO.PersonDetailsDTOBuilder givenPersonDetailsDTOBuilder() {
        return PersonDetailsDTO.builder()
                .uuid("123e4567-e89b-12d3-a456-426614174000")
                .id("1")
                .name("John Doe")
                .likes("Reading")
                .transport("Car")
                .averageSpeed("50")
                .topSpeed("120");
    }
}