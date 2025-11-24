package com.technicaltest.fileprocessor.person.web;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technicaltest.fileprocessor.BaseIntegrationTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PersonDetailsControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void resetWireMock() {
        wireMockServer.resetAll();
    }

    @Test
    public void uploadFile_validFile_returns200AndJson() throws Exception {
        String validLine = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test_file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                validLine.getBytes()
        );

        stubForIPAPI.returnsIPAddressInfo("{\"status\":\"success\",\"country\":\"GB\",\"isp\":\"AllowedISP\"}");
        
        MvcResult result = mockMvc.perform(multipart("/person-details/file").file(file))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"OutcomeFile.json\""))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = "[{\"name\":\"John Smith\",\"transport\":\"Rides A Bike\",\"topSpeed\":12.1}]";
        assertThat(responseBody).isEqualToIgnoringWhitespace(expectedJson);
    }

    @Test
    public void uploadFile_invalidFile_returns400() throws Exception {
        String invalidLine = "bad|data|only|four|fields";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test_file_invalid.txt",
                MediaType.TEXT_PLAIN_VALUE,
                invalidLine.getBytes()
        );
        
        stubForIPAPI.returnsIPAddressInfo("{\"status\":\"success\",\"country\":\"GB\",\"isp\":\"AllowedISP\"}");
        
        MvcResult result = mockMvc.perform(multipart("/person-details/file").file(file))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.readValue(responseBody, Map.class);

        assertThat(responseMap.get("message")).isEqualTo("File validation failed");
        List<Map<String, Object>> problems = (List<Map<String, Object>>) responseMap.get("problems");
        assertThat(problems).isNotEmpty();
        Map<String, Object> firstProblem = problems.get(0);
        assertThat(firstProblem.get("identifier")).isEqualTo("1");
        assertThat(firstProblem.get("identifierType")).isEqualTo("Line Number");
        assertThat(firstProblem.get("description")).asString().contains("Incorrect number of fields");
    }

    @Test
    public void uploadFile_blockedCountryOrIsp_returns403() throws Exception {
        String validLine = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test_file_blocked.txt",
                MediaType.TEXT_PLAIN_VALUE,
                validLine.getBytes()
        );
        
        stubForIPAPI.returnsIPAddressInfo("{\"status\":\"success\",\"country\":\"China\",\"isp\":\"AWS\"}");
        
        MvcResult result = mockMvc.perform(multipart("/person-details/file").file(file))
                .andExpect(status().isForbidden())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.readValue(responseBody, Map.class);

        assertThat(responseMap.get("message")).isEqualTo("IP Address Validation Failed");
        List<Map<String, Object>> problems = (List<Map<String, Object>>) responseMap.get("problems");
        assertThat(problems).hasSizeGreaterThanOrEqualTo(1);
        boolean hasCountryMsg = problems.stream()
                .map(p -> p.get("description").toString())
                .anyMatch(s -> s.contains("Access from country 'China' is blocked"));
        boolean hasIspMsg = problems.stream()
                .map(p -> p.get("description").toString())
                .anyMatch(s -> s.contains("Access from Internet Service Provider 'AWS' is blocked"));
        assertThat(hasCountryMsg).isTrue();
        assertThat(hasIspMsg).isTrue();
    }
}
