package com.mg.apicaller.service;

import com.mg.apicaller.builder.ApiCallResponseBuilder;
import com.mg.apicaller.builder.HeadersBuilder;
import com.mg.apicaller.builder.UrlBuilder;
import com.mg.apicaller.dto.ApiCallRequest;
import com.mg.apicaller.dto.ApiCallResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ApiCallService {
    private final RestTemplate restTemplate;
    private final UrlBuilder urlBuilder;
    private final HeadersBuilder headersBuilder;
    private final ApiCallResponseBuilder apiCallResponseBuilder;

    public ApiCallResponse makeCall(ApiCallRequest apiCallRequest) {
        try {
            // set call params
            var url = urlBuilder.buildUrl(apiCallRequest);
            var method = HttpMethod.valueOf(apiCallRequest.getMethod());
            var headers = headersBuilder.buildHeaders(apiCallRequest);
            var request = new HttpEntity<>(apiCallRequest.getBody(), headers);
            // make call
            var response = restTemplate.exchange(url, method, request, String.class);
            // process successful response
            return apiCallResponseBuilder.buildApiCallResponse(response);
        } catch (HttpStatusCodeException e) {
            // process not successful response
            return apiCallResponseBuilder.buildApiCallResponse(e);
        } catch (Exception e) {
            // process unknown error
            return apiCallResponseBuilder.buildApiCallResponse(e);
        }
    }
}
