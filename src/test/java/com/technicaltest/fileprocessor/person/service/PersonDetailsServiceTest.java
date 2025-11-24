package com.technicaltest.fileprocessor.person.service;

import com.technicaltest.fileprocessor.person.domain.PersonDetails;
import com.technicaltest.fileprocessor.person.domain.PersonDetailsDTO;
import com.technicaltest.fileprocessor.person.transformer.PersonDetailsTransformer;
import com.technicaltest.fileprocessor.person.validation.PersonDetailsDTOValidator;
import com.technicaltest.fileprocessor.person.validation.PersonDetailsFileValidator;
import com.technicaltest.fileprocessor.request.repository.RequestRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static com.technicaltest.fileprocessor.person.fixture.PersonDetailsDTOFixture.givenPersonDetailsDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonDetailsServiceTest {

    @Mock
    RequestRepository requestRepository;

    @Mock
    PersonDetailsFileValidator fileValidator;

    @Mock
    PersonDetailsTransformer personDetailsTransformer;

    @Mock
    PersonDetailsDTOValidator personDetailsDTOValidator;

    @InjectMocks
    PersonDetailsService service;

    @Test
    void processFile_parsesPersonDetails_andCallsValidators() throws Exception {
        String line = "uuid-1|id-1|Alice|likes|Car|12.3|15.5";
        InputStream inputStream = new ByteArrayInputStream(line.getBytes());

        PersonDetailsDTO personDetailsDTO = givenPersonDetailsDTO();

        doNothing().when(fileValidator).validateFile(List.of(line));
        doNothing().when(personDetailsDTOValidator).validatePersonDetailsDTOs(anyList());
        when(personDetailsTransformer.transformFileLineToPersonDetailsDTO(anyString())).thenReturn(personDetailsDTO);

        List<PersonDetails> result = service.processPersonDetailsFile(inputStream);

        assertThat(result).hasSize(1);
        PersonDetails p = result.get(0);
        assertThat(p.getName()).isEqualTo(personDetailsDTO.getName());
        assertThat(p.getTransport()).isEqualTo(personDetailsDTO.getTransport());
        assertThat(p.getTopSpeed()).isEqualByComparingTo(new BigDecimal(personDetailsDTO.getTopSpeed()));
    }
}
