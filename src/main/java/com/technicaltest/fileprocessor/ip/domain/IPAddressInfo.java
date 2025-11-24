package com.technicaltest.fileprocessor.ip.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IPAddressInfo {
    
    private String query;
    private String status;
    private String message;
    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zipCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String timezone;
    private String internetServiceProvider;
    private String organisationName;
    private String as;
}
