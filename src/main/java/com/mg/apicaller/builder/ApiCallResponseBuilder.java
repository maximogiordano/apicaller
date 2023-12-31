package com.mg.apicaller.builder;

import com.mg.apicaller.dto.ApiCallResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Component
@RequiredArgsConstructor
public class ApiCallResponseBuilder {
    private final ApiCallResponseHeadersBuilder apiCallResponseHeadersBuilder;

    public ApiCallResponse buildApiCallResponse(ResponseEntity<String> response) {
        var apiCallResponse = new ApiCallResponse();

        apiCallResponse.setStatus(response.getStatusCode().value());
        apiCallResponse.setHeaders(apiCallResponseHeadersBuilder.buildApiCallResponseHeaders(response.getHeaders()));
        apiCallResponse.setBody(response.getBody());

        return apiCallResponse;
    }

    public ApiCallResponse buildApiCallResponse(HttpStatusCodeException e) {
        var apiCallResponse = new ApiCallResponse();

        apiCallResponse.setStatus(e.getStatusCode().value());
        apiCallResponse.setHeaders(apiCallResponseHeadersBuilder.buildApiCallResponseHeaders(e.getResponseHeaders()));
        apiCallResponse.setBody(e.getResponseBodyAsString());

        return apiCallResponse;
    }

    public ApiCallResponse buildApiCallResponse(Exception e) {
        var apiCallResponse = new ApiCallResponse();

        apiCallResponse.setBody(e.toString());

        return apiCallResponse;
    }
}
