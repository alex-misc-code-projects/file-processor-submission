package com.technicaltest.fileprocessor.person.transformer;

import org.junit.jupiter.api.Test;

import com.technicaltest.fileprocessor.person.domain.PersonDetailsDTO;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonDetailsTransformerTest {

    private final PersonDetailsTransformer transformer = new PersonDetailsTransformer();

    @Test
    public void transformFileLinesToPersonDetailsDTO() {
        String line = "uuid123|id456|Jane Doe|Likes Apples|Drives Car|5.5|10.2";
        PersonDetailsDTO dto = transformer.transformFileLineToPersonDetailsDTO(line);
        PersonDetailsDTO expected = PersonDetailsDTO.builder()
                .uuid("uuid123")
                .id("id456")
                .name("Jane Doe")
                .likes("Likes Apples")
                .transport("Drives Car")
                .averageSpeed("5.5")
                .topSpeed("10.2")
                .build();

        assertThat(dto).isEqualTo(expected);
    }
}
