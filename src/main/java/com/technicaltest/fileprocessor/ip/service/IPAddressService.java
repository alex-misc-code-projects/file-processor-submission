package com.technicaltest.fileprocessor.ip.service;

import org.springframework.stereotype.Service;

import com.technicaltest.fileprocessor.ip.domain.IPAddressInfo;
import com.technicaltest.fileprocessor.ip.transformer.IPAddressInfoTransformer;
import com.technicaltest.fileprocessor.subsystem.ipapi.domain.IPAddressInfoDTO;
import com.technicaltest.fileprocessor.subsystem.ipapi.provider.IPAPIProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IPAddressService {

    private final IPAPIProvider ipapiProvider;
    private final IPAddressInfoTransformer transformer;

    public IPAddressInfo getIPAddressInfo(String ipAddress) {
        IPAddressInfoDTO dto = ipapiProvider.getIPAddressInfo(ipAddress);
        return transformer.transform(dto);
    }
}
