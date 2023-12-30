package com.mg.apicaller.service;

import com.mg.apicaller.dto.ApiCallRequest;
import com.mg.apicaller.dto.ApiCallResponse;
import com.mg.apicaller.dto.KeyValuePair;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ApiCallService {
    private final RestTemplate restTemplate;

    public ApiCallResponse makeCall(ApiCallRequest apiCallRequest) {
        try {
            // set call params
            var url = getUrl(apiCallRequest);
            var method = HttpMethod.valueOf(apiCallRequest.getMethod());
            var headers = CollectionUtils.toMultiValueMap(getHeadersAsMap(apiCallRequest));
            var request = new HttpEntity<>(apiCallRequest.getBody(), headers);
            // make call
            var response = restTemplate.exchange(url, method, request, String.class);
            // process successful response
            return toApiCallResponse(response);
        } catch (HttpStatusCodeException e) {
            // process not successful response
            return toApiCallResponse(e);
        } catch (Exception e) {
            // process unknown error
            return toApiCallResponse(e);
        }
    }

    private String getUrl(ApiCallRequest apiCallRequest) {
        String url = apiCallRequest.getUri();

        if (apiCallRequest.getParams() != null && !apiCallRequest.getParams().isEmpty()) {
            url += "?" + apiCallRequest.getParams()
                    .stream()
                    .map(this::toParam)
                    .collect(joining("&"));
        }

        return url;
    }

    private String toParam(KeyValuePair keyValuePair) {
        return URLEncoder.encode(keyValuePair.getKey(), Charset.defaultCharset()) + "=" +
                URLEncoder.encode(keyValuePair.getValue(), Charset.defaultCharset());
    }

    private Map<String, List<String>> getHeadersAsMap(ApiCallRequest apiCallRequest) {
        return apiCallRequest.getHeaders()
                .stream()
                .collect(groupingBy(KeyValuePair::getKey, mapping(KeyValuePair::getValue, toList())));
    }

    private ApiCallResponse toApiCallResponse(ResponseEntity<String> response) {
        var apiCallResponse = new ApiCallResponse();

        apiCallResponse.setStatus(response.getStatusCode().value());
        apiCallResponse.setHeaders(response.getHeaders().entrySet().stream().map(this::toKeyValuePair).toList());
        apiCallResponse.setBody(response.getBody());

        return apiCallResponse;
    }

    private KeyValuePair toKeyValuePair(Map.Entry<String, List<String>> entry) {
        var keyValuePair = new KeyValuePair();

        keyValuePair.setKey(entry.getKey());
        keyValuePair.setValue(toValue(entry.getValue()));

        return keyValuePair;
    }

    private String toValue(List<String> value) {
        if (value == null) {
            return "<null>";
        }

        if (value.isEmpty()) {
            return "<empty>";
        }

        if (value.size() == 1) {
            return value.get(0);
        }

        return value.toString();
    }

    private ApiCallResponse toApiCallResponse(HttpStatusCodeException e) {
        var apiCallResponse = new ApiCallResponse();

        apiCallResponse.setStatus(e.getStatusCode().value());

        var responseHeaders = e.getResponseHeaders();
        if (responseHeaders != null) {
            apiCallResponse.setHeaders(responseHeaders.entrySet().stream().map(this::toKeyValuePair).toList());
        }

        apiCallResponse.setBody(e.getResponseBodyAsString());

        return apiCallResponse;
    }

    private ApiCallResponse toApiCallResponse(Exception e) {
        var apiCallResponse = new ApiCallResponse();

        apiCallResponse.setBody(e.toString());

        return apiCallResponse;
    }
}
