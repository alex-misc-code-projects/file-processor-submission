package com.technicaltest.fileprocessor.fixture;

import com.technicaltest.fileprocessor.ip.domain.IPAddressInfo;

import java.math.BigDecimal;

public class IPAddressInfoFixture {

    public static IPAddressInfo givenIPAddressInfo() {
        return givenIPAddressInfoBuilder().build();
    }

    public static IPAddressInfo.IPAddressInfoBuilder givenIPAddressInfoBuilder() {
        return IPAddressInfo.builder()
                .query("1.2.3.4")
                .status("ok")
                .message(null)
                .country("Country")
                .countryCode("CC")
                .region("R")
                .regionName("Region")
                .city("City")
                .zipCode("00000")
                .latitude(new BigDecimal("0"))
                .longitude(new BigDecimal("0"))
                .timezone("UTC")
                .internetServiceProvider("ISP")
                .organisationName("Org")
                .as("AS0 Org");
    }
}
