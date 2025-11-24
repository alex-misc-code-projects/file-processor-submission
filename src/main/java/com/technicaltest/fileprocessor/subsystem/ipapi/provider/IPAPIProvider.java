package com.technicaltest.fileprocessor.subsystem.ipapi.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.technicaltest.fileprocessor.ip.exception.IPAPIException;
import com.technicaltest.fileprocessor.subsystem.ipapi.domain.IPAddressInfoDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IPAPIProvider {

    private final RestClient restClient;

    @Value("${ip-api.base-url}")
    private String baseUrl;

    public IPAddressInfoDTO getIPAddressInfo(String ipAddress) {
        IPAddressInfoDTO response = restClient.get()
            .uri(String.format("%s/json/%s", baseUrl, ipAddress))
            .retrieve()
            .body(IPAddressInfoDTO.class);

        if (response == null || response.getStatus() == null || !response.getStatus().equals("success")) {
            String message = (response == null) ? "null response" : response.getMessage();
            throw new IPAPIException(String.format("Failed to retrieve IP address info: %s", message));
        }
        return response;
    }
}
