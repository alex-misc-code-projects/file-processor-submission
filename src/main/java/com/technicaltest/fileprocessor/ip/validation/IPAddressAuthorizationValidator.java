package com.technicaltest.fileprocessor.ip.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.technicaltest.fileprocessor.ip.domain.IPAddressInfo;
import com.technicaltest.fileprocessor.ip.exception.IPAddressAuthorizationValidationException;
import com.technicaltest.fileprocessor.shared.domain.Problem;

@Component
public class IPAddressAuthorizationValidator {

    @Value("${fileprocessor.validation.ipaddress.blockedCountries}")
    private List<String> blockedCountries;

    @Value("${fileprocessor.validation.ipaddress.blockedISPs}")
    private List<String> blockedISPs;
    
    public void validateIPAddressIsAuthorized(IPAddressInfo ipAddressInfo) {
        List<Problem> problems = Stream.of(
            validateCountry(ipAddressInfo),
            validateIsp(ipAddressInfo)
        ).flatMap(List::stream).collect(Collectors.toList());
        if (!problems.isEmpty()) {
            throw new IPAddressAuthorizationValidationException("IP Address Validation Failed", problems);
        }
    }

    private List<Problem> validateCountry(IPAddressInfo ipAddressInfo) {
        List<Problem> problems = new ArrayList<>();
        if (ipAddressInfo.getCountry() == null || ipAddressInfo.getCountry().isEmpty()) {
            problems.add(Problem.builder()
                .description("Country information is missing from IP address data")
                .build());
                return problems;
        }
        if (blockedCountries.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(ipAddressInfo.getCountry().toLowerCase())) {
            problems.add(Problem.builder()
                .description(String.format("Access from country '%s' is blocked", ipAddressInfo.getCountry()))
                .build());
        }
        return problems;
    }

    private List<Problem> validateIsp(IPAddressInfo ipAddressInfo) {
        List<Problem> problems = new ArrayList<>();
        if (ipAddressInfo.getInternetServiceProvider() == null || ipAddressInfo.getInternetServiceProvider().isEmpty()) {
            problems.add(Problem.builder()
                .description("Internet Service Provider information is missing from IP address data")
                .build());
                return problems;
        }
        if (blockedISPs.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(ipAddressInfo.getInternetServiceProvider().toLowerCase())) {
            problems.add(Problem.builder()
                .description(String.format("Access from Internet Service Provider '%s' is blocked", ipAddressInfo.getInternetServiceProvider()))
                .build());
        }
        return problems;
    }
}
