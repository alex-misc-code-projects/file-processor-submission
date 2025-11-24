package com.technicaltest.fileprocessor.fixture;

import com.technicaltest.fileprocessor.subsystem.ipapi.domain.IPAddressInfoDTO;

import java.math.BigDecimal;

public class IPAddressInfoDTOFixture {

    public static IPAddressInfoDTO givenIPAddressInfoDTO() {
        return givenIPAddressInfoDTOBuilder().build();
    }

    public static IPAddressInfoDTO.IPAddressInfoDTOBuilder givenIPAddressInfoDTOBuilder() {
        return IPAddressInfoDTO.builder()
                .query("1.2.3.4")
                .status("ok")
                .message(null)
                .country("Country")
                .countryCode("CC")
                .region("R")
                .regionName("Region")
                .city("City")
                .zip("00000")
                .lat(new BigDecimal("0"))
                .lon(new BigDecimal("0"))
                .timezone("UTC")
                .isp("ISP")
                .org("Org")
                .as("AS0 Org");
    }
}
