package com.technicaltest.fileprocessor.stub;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

public class StubForIPAPI {

    private final WireMockServer wireMockServer;

    public StubForIPAPI(WireMockServer wireMockServer) {
        this.wireMockServer = wireMockServer;
    }

    public void returnsIPAddressInfo(String responseBody) {
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathMatching("/json/.*"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
    }
}
