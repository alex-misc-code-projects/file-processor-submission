package com.technicaltest.fileprocessor.ip.validation;

import com.technicaltest.fileprocessor.ip.domain.IPAddressInfo;
import com.technicaltest.fileprocessor.ip.exception.IPAddressAuthorizationValidationException;
import com.technicaltest.fileprocessor.shared.domain.Problem;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.technicaltest.fileprocessor.fixture.IPAddressInfoFixture.givenIPAddressInfoBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IPAddressValidatorTest {

    private final IPAddressAuthorizationValidator validator = new IPAddressAuthorizationValidator();

    @Test
    public void validateIPAddressInfo_missingCountry_reportsProblem() throws Exception {
        setBlockedCountries(List.of("neverland"));
        setBlockedISPs(List.of());

        IPAddressInfo info = givenValidIPAddressInfo()
            .country(null)
            .internetServiceProvider("Some ISP")
            .build();

        IPAddressAuthorizationValidationException ex = assertThrows(IPAddressAuthorizationValidationException.class,
            () -> validator.validateIPAddressIsAuthorized(info));

        List<Problem> problems = ex.getProblems();
        assertThat(problems).hasSize(1);
        assertThat(problems.get(0).getDescription()).contains("Country information is missing");
    }

    @Test
    public void validateIPAddressInfo_blockedCountry_reportsProblem() throws Exception {
        setBlockedCountries(List.of("neverland"));
        setBlockedISPs(List.of());

        IPAddressInfo info = givenValidIPAddressInfo()
            .country("Neverland")
            .internetServiceProvider("ISP Inc")
            .build();

        IPAddressAuthorizationValidationException ex = assertThrows(IPAddressAuthorizationValidationException.class,
            () -> validator.validateIPAddressIsAuthorized(info));

        List<Problem> problems = ex.getProblems();
        assertThat(problems).hasSize(1);
        assertThat(problems.get(0).getDescription()).contains("Access from country 'Neverland' is blocked");
    }

    @Test
    public void validateIPAddressInfo_blockedIsp_reportsProblem() throws Exception {
        setBlockedCountries(List.of());
        setBlockedISPs(List.of("badisp"));

        IPAddressInfo info = givenValidIPAddressInfo()
            .country("Somewhere")
            .internetServiceProvider("BadISP")
            .build();

        IPAddressAuthorizationValidationException ex = assertThrows(IPAddressAuthorizationValidationException.class,
            () -> validator.validateIPAddressIsAuthorized(info));

        List<Problem> problems = ex.getProblems();
        assertThat(problems).hasSize(1);
        assertThat(problems.get(0).getDescription()).contains("Access from Internet Service Provider 'BadISP' is blocked");
    }

    private void setBlockedCountries(List<String> countries) throws Exception {
        ReflectionTestUtils.setField(validator, "blockedCountries", countries);
    }

    private void setBlockedISPs(List<String> isps) throws Exception {
        ReflectionTestUtils.setField(validator, "blockedISPs", isps);
    }

    private IPAddressInfo.IPAddressInfoBuilder givenValidIPAddressInfo() {
        return givenIPAddressInfoBuilder();
    }
}
