package com.technicaltest.fileprocessor.ip.transformer;

import org.junit.jupiter.api.Test;

import com.technicaltest.fileprocessor.fixture.IPAddressInfoDTOFixture;
import com.technicaltest.fileprocessor.ip.domain.IPAddressInfo;
import com.technicaltest.fileprocessor.subsystem.ipapi.domain.IPAddressInfoDTO;

import static org.assertj.core.api.Assertions.assertThat;

public class IPAddressInfoTransformerTest {

    private final IPAddressInfoTransformer transformer = new IPAddressInfoTransformer();

    @Test
    public void transformFromDTOToInternalDomain() {
        IPAddressInfoDTO dto = IPAddressInfoDTOFixture.givenIPAddressInfoDTO();

        IPAddressInfo result = transformer.transform(dto);

        IPAddressInfo expected = IPAddressInfo.builder()
                .query(dto.getQuery())
                .status(dto.getStatus())
                .message(dto.getMessage())
                .country(dto.getCountry())
                .countryCode(dto.getCountryCode())
                .region(dto.getRegion())
                .regionName(dto.getRegionName())
                .city(dto.getCity())
                .zipCode(dto.getZip())
                .latitude(dto.getLat())
                .longitude(dto.getLon())
                .timezone(dto.getTimezone())
                .internetServiceProvider(dto.getIsp())
                .organisationName(dto.getOrg())
                .as(dto.getAs())
                .build();

        assertThat(result).isEqualTo(expected);
    }
}
