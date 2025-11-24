package com.technicaltest.fileprocessor.subsystem.ipapi.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IPAddressInfoDTO {

    private String query;
    private String status;
    private String message;
    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private BigDecimal lat;
    private BigDecimal lon;
    private String timezone;
    private String isp;
    private String org;
    private String as;
}
