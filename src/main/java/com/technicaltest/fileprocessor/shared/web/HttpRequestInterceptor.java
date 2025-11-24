package com.technicaltest.fileprocessor.shared.web;

import java.time.Instant;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.technicaltest.fileprocessor.ip.domain.IPAddressInfo;
import com.technicaltest.fileprocessor.ip.service.IPAddressService;
import com.technicaltest.fileprocessor.ip.validation.IPAddressAuthorizationValidator;
import com.technicaltest.fileprocessor.request.domain.Request;
import com.technicaltest.fileprocessor.request.repository.RequestRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HttpRequestInterceptor implements HandlerInterceptor {

    private final IPAddressService ipAddressService;
    private final RequestRepository requestRepository;
    private final IPAddressAuthorizationValidator ipAddressValidator;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        IPAddressInfo ipAddressInfo = ipAddressService.getIPAddressInfo(clientIp);
        ipAddressValidator.validateIPAddressIsAuthorized(ipAddressInfo);

        request.setAttribute("ipAddressInfo", ipAddressInfo);
        request.setAttribute("clientIp", clientIp);
        request.setAttribute("happenedAt", Instant.now());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        saveRequestIfNeeded(request, response, null);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        saveRequestIfNeeded(request, response, ex);
    }

    private void saveRequestIfNeeded(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        if (Boolean.TRUE.equals(request.getAttribute("requestSaved"))) {
            return;
        }

        Instant happenedAt = (Instant) request.getAttribute("happenedAt");
        if (happenedAt == null) {
            happenedAt = Instant.now();
        }

        Instant completedAt = Instant.now();
        IPAddressInfo ipAddressInfo = (IPAddressInfo) request.getAttribute("ipAddressInfo");

        Integer status = response != null ? response.getStatus() : null;
        if (status == null && ex != null) {
            status = 500;
        }

        String clientIp = (String) request.getAttribute("clientIp");
        String country = ipAddressInfo != null ? ipAddressInfo.getCountry() : null;
        String isp = ipAddressInfo != null ? ipAddressInfo.getInternetServiceProvider() : null;
        Long millis = completedAt.toEpochMilli() - happenedAt.toEpochMilli();

        requestRepository.save(Request.builder()
            .uri(request.getRequestURI())
            .happenedAt(happenedAt)
            .httpResponseCode(status)
            .requesterIpAddress(clientIp)
            .requesterCountry(country)
            .requesterInternetServiceProvider(isp)
            .millisecondsTakenToCompleteRequest(millis)
            .build());

        request.setAttribute("requestSaved", Boolean.TRUE);
    }
}
