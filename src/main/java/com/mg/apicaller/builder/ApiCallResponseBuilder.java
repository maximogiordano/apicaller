package com.mg.apicaller.builder;

import com.mg.apicaller.dto.ApiCallResponse;
import com.mg.apicaller.dto.KeyValuePair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;
import java.util.Map;

@Component
public class ApiCallResponseBuilder {
    public ApiCallResponse buildApiCallResponse(ResponseEntity<String> response) {
        var apiCallResponse = new ApiCallResponse();

        apiCallResponse.setStatus(response.getStatusCode().value());
        apiCallResponse.setHeaders(response.getHeaders().entrySet().stream().map(this::toKeyValuePair).toList());
        apiCallResponse.setBody(response.getBody());

        return apiCallResponse;
    }

    private KeyValuePair toKeyValuePair(Map.Entry<String, List<String>> entry) {
        var keyValuePair = new KeyValuePair();

        keyValuePair.setKey(entry.getKey());
        keyValuePair.setValue(toSingleValue(entry.getValue()));

        return keyValuePair;
    }

    private String toSingleValue(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        if (list.size() == 1) {
            return list.get(0);
        }

        return list.toString();
    }

    public ApiCallResponse buildApiCallResponse(HttpStatusCodeException e) {
        var apiCallResponse = new ApiCallResponse();

        apiCallResponse.setStatus(e.getStatusCode().value());

        var responseHeaders = e.getResponseHeaders();
        if (responseHeaders != null) {
            apiCallResponse.setHeaders(responseHeaders.entrySet().stream().map(this::toKeyValuePair).toList());
        }

        apiCallResponse.setBody(e.getResponseBodyAsString());

        return apiCallResponse;
    }

    public ApiCallResponse buildApiCallResponse(Exception e) {
        var apiCallResponse = new ApiCallResponse();

        apiCallResponse.setBody(e.toString());

        return apiCallResponse;
    }
}
