package com.technicaltest.fileprocessor.shared.web;

import static org.assertj.core.api.Assertions.assertThat;
 

import java.util.ArrayList;
import java.util.List;

import com.technicaltest.fileprocessor.BaseIntegrationTest;
import com.technicaltest.fileprocessor.fixture.IPAddressInfoFixture;
import com.technicaltest.fileprocessor.ip.domain.IPAddressInfo;
import com.technicaltest.fileprocessor.request.domain.Request;
import com.technicaltest.fileprocessor.request.repository.RequestRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
 
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class HttpRequestInterceptorIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private HttpRequestInterceptor interceptor;

    @Autowired
    private RequestRepository requestRepository;

    // Use the real IPAddressService which will call the stubbed IP API (WireMock)

    @BeforeEach
    void setUp() {
        requestRepository.deleteAll();
    }

    @Test
    void preHandle_then_postHandle_savesRequestToRepository() throws Exception {
    IPAddressInfo info = IPAddressInfoFixture.givenIPAddressInfo();
    stubForIPAPI.returnsIPAddressInfo(String.format("{\"status\":\"success\",\"country\":\"%s\",\"isp\":\"%s\"}", 
        info.getCountry(), info.getInternetServiceProvider()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("1.2.3.4");
        request.setRequestURI("/sample/endpoint");

        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(200);

        boolean cont = interceptor.preHandle(request, response, new Object());
        assertThat(cont).isTrue();

        interceptor.postHandle(request, response, new Object(), null);

        Iterable<Request> saved = requestRepository.findAll();
        List<Request> list = new ArrayList<>();
        saved.forEach(list::add);

        assertThat(list).hasSize(1);
        Request savedReq = list.get(0);
        assertThat(savedReq.getUri()).isEqualTo("/sample/endpoint");
        assertThat(savedReq.getRequesterIpAddress()).isEqualTo("1.2.3.4");
        assertThat(savedReq.getRequesterCountry()).isEqualTo(info.getCountry());
        assertThat(savedReq.getRequesterInternetServiceProvider()).isEqualTo(info.getInternetServiceProvider());
        assertThat(savedReq.getHttpResponseCode()).isEqualTo(200);
        assertThat(savedReq.getMillisecondsTakenToCompleteRequest()).isNotNull();
    }

    @Test
    void preHandle_then_afterCompletion_onException_savesRequestWithStatus400() throws Exception {
        IPAddressInfo info = IPAddressInfoFixture.givenIPAddressInfo();
        stubForIPAPI.returnsIPAddressInfo(String.format("{\"status\":\"success\",\"country\":\"%s\",\"isp\":\"%s\"}",
            info.getCountry(), info.getInternetServiceProvider()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("1.2.3.4");
        request.setRequestURI("/sample/endpoint");

        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(400);

        boolean cont = interceptor.preHandle(request, response, new Object());
        assertThat(cont).isTrue();

        Exception ex = new RuntimeException("simulated error");
        interceptor.afterCompletion(request, response, new Object(), ex);

        Iterable<Request> saved = requestRepository.findAll();
        List<Request> list = new ArrayList<>();
        saved.forEach(list::add);

        assertThat(list).hasSize(1);
        Request savedReq = list.get(0);
        assertThat(savedReq.getUri()).isEqualTo("/sample/endpoint");
        assertThat(savedReq.getRequesterIpAddress()).isEqualTo("1.2.3.4");
        assertThat(savedReq.getRequesterCountry()).isEqualTo(info.getCountry());
        assertThat(savedReq.getRequesterInternetServiceProvider()).isEqualTo(info.getInternetServiceProvider());
        assertThat(savedReq.getHttpResponseCode()).isEqualTo(400);
        assertThat(savedReq.getMillisecondsTakenToCompleteRequest()).isNotNull();
    }
}
