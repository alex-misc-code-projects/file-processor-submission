package com.technicaltest.fileprocessor.ip.transformer;

import org.springframework.stereotype.Component;

import com.technicaltest.fileprocessor.ip.domain.IPAddressInfo;
import com.technicaltest.fileprocessor.subsystem.ipapi.domain.IPAddressInfoDTO;

@Component
public class IPAddressInfoTransformer {

    public IPAddressInfo transform(IPAddressInfoDTO dto) {
        return IPAddressInfo.builder()
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
    }
}
