package com.technicaltest.fileprocessor.person.service;

import org.springframework.stereotype.Service;

import com.technicaltest.fileprocessor.person.domain.PersonDetails;
import com.technicaltest.fileprocessor.person.domain.PersonDetailsDTO;
import com.technicaltest.fileprocessor.person.transformer.PersonDetailsTransformer;
import com.technicaltest.fileprocessor.person.validation.PersonDetailsDTOValidator;
import com.technicaltest.fileprocessor.person.validation.PersonDetailsFileValidator;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonDetailsService {

    private final PersonDetailsFileValidator personDetailsFileValidator;
    private final PersonDetailsTransformer personDetailsTransformer;
    private final PersonDetailsDTOValidator personDetailsDTOValidator;

    public List<PersonDetails> processPersonDetailsFile(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> lines = reader.lines().collect(Collectors.toList());
        reader.close();
        personDetailsFileValidator.validateFile(lines);
        
        List<PersonDetailsDTO> personDetailsDTOs = lines.stream()
                .map(line -> personDetailsTransformer.transformFileLineToPersonDetailsDTO(line))
                .toList();
        return processPersonDetailsDTOs(personDetailsDTOs);
    }

    private List<PersonDetails> processPersonDetailsDTOs(List<PersonDetailsDTO> personDetailsDTOs) {
        personDetailsDTOValidator.validatePersonDetailsDTOs(personDetailsDTOs);

        List<PersonDetails> personDetailsList = new ArrayList<>();
        personDetailsDTOs.forEach(personDetailsDTO -> {
            personDetailsList.add(PersonDetails.builder()
                    .name(personDetailsDTO.getName())
                    .transport(personDetailsDTO.getTransport())
                    .topSpeed(personDetailsDTO.getTopSpeed().isEmpty() ? null : new BigDecimal(personDetailsDTO.getTopSpeed()))
                    .build());
        });
        return personDetailsList;
    }
}