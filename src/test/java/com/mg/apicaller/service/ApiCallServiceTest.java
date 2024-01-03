package com.mg.apicaller.service;

import com.mg.apicaller.builder.ApiCallResponseBuilder;
import com.mg.apicaller.builder.HeadersBuilder;
import com.mg.apicaller.builder.UrlBuilder;
import com.mg.apicaller.dto.ApiCallRequest;
import com.mg.apicaller.dto.ApiCallResponse;
import com.mg.apicaller.dto.KeyValuePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiCallServiceTest {
    @InjectMocks
    ApiCallService apiCallService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    UrlBuilder urlBuilder;

    @Mock
    HeadersBuilder headersBuilder;

    @Mock
    ApiCallResponseBuilder apiCallResponseBuilder;

    @Test
    void testSuccessfulResponse() {
        var requestMethod = "POST";
        var requestUri = "http://foo.com";
        var requestParams = List.of(new KeyValuePair("param", "value"));
        var requestHeaders = List.of(new KeyValuePair("header", "value"));
        var requestBody = "{\"attribute\":\"value\"}";
        var apiCallRequest = new ApiCallRequest();
        var requestUrl = "http://foo.com?param=value";
        var requestHeadersMultiValueMap = CollectionUtils.toMultiValueMap(Map.of("header", List.of("value")));
        var requestEntity = new HttpEntity<>(requestBody, requestHeadersMultiValueMap);
        var responseStatus = 200;
        var responseHeadersMultiValueMap = CollectionUtils.toMultiValueMap(Map.of("header", List.of("value")));
        var responseHttpHeaders = new HttpHeaders(responseHeadersMultiValueMap);
        var responseHeaders = List.of(new KeyValuePair("header", "value"));
        var responseBody = "{\"attribute\":\"value\"}";
        var response = ResponseEntity.status(responseStatus).headers(responseHttpHeaders).body(responseBody);
        var apiCallResponse = new ApiCallResponse();

        apiCallRequest.setMethod(requestMethod);
        apiCallRequest.setUri(requestUri);
        apiCallRequest.setParams(requestParams);
        apiCallRequest.setHeaders(requestHeaders);
        apiCallRequest.setBody(requestBody);

        apiCallResponse.setStatus(responseStatus);
        apiCallResponse.setHeaders(responseHeaders);
        apiCallResponse.setBody(responseBody);

        when(urlBuilder.buildUrl(apiCallRequest)).thenReturn(requestUrl);
        when(headersBuilder.buildHeaders(apiCallRequest)).thenReturn(requestHeadersMultiValueMap);
        when(restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, String.class)).thenReturn(response);
        when(apiCallResponseBuilder.buildApiCallResponse(response)).thenReturn(apiCallResponse);

        var actualResponse = apiCallService.makeCall(apiCallRequest);

        verify(urlBuilder).buildUrl(apiCallRequest);
        verify(headersBuilder).buildHeaders(apiCallRequest);
        verify(restTemplate).exchange(requestUrl, HttpMethod.POST, requestEntity, String.class);
        verify(apiCallResponseBuilder).buildApiCallResponse(response);
        assertEquals(apiCallResponse, actualResponse);
    }

    @Test
    void testUnsuccessfulResponse() {
        var requestMethod = "POST";
        var requestUri = "http://foo.com";
        var requestParams = List.of(new KeyValuePair("param", "value"));
        var requestHeaders = List.of(new KeyValuePair("header", "value"));
        var requestBody = "{\"attribute\":\"value\"}";
        var apiCallRequest = new ApiCallRequest();
        var requestUrl = "http://foo.com?param=value";
        var requestHeadersMultiValueMap = CollectionUtils.toMultiValueMap(Map.of("header", List.of("value")));
        var requestEntity = new HttpEntity<>(requestBody, requestHeadersMultiValueMap);
        var responseStatus = 404;
        var responseHeaders = List.of(new KeyValuePair("header", "value"));
        var responseBody = "{\"message\":\"resource not found\"}";
        var e = mock(HttpStatusCodeException.class);
        var apiCallResponse = new ApiCallResponse();

        apiCallRequest.setMethod(requestMethod);
        apiCallRequest.setUri(requestUri);
        apiCallRequest.setParams(requestParams);
        apiCallRequest.setHeaders(requestHeaders);
        apiCallRequest.setBody(requestBody);

        apiCallResponse.setStatus(responseStatus);
        apiCallResponse.setHeaders(responseHeaders);
        apiCallResponse.setBody(responseBody);

        when(urlBuilder.buildUrl(apiCallRequest)).thenReturn(requestUrl);
        when(headersBuilder.buildHeaders(apiCallRequest)).thenReturn(requestHeadersMultiValueMap);
        when(restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, String.class)).thenThrow(e);
        when(apiCallResponseBuilder.buildApiCallResponse(e)).thenReturn(apiCallResponse);

        var actualResponse = apiCallService.makeCall(apiCallRequest);

        verify(urlBuilder).buildUrl(apiCallRequest);
        verify(headersBuilder).buildHeaders(apiCallRequest);
        verify(restTemplate).exchange(requestUrl, HttpMethod.POST, requestEntity, String.class);
        verify(apiCallResponseBuilder).buildApiCallResponse(e);
        assertEquals(apiCallResponse, actualResponse);
    }

    @Test
    void testUnexpectedError() {
        var requestMethod = "POST";
        var requestUri = "http://foo.com";
        var requestParams = List.of(new KeyValuePair("param", "value"));
        var requestHeaders = List.of(new KeyValuePair("header", "value"));
        var requestBody = "{\"attribute\":\"value\"}";
        var apiCallRequest = new ApiCallRequest();
        var requestUrl = "http://foo.com?param=value";
        var requestHeadersMultiValueMap = CollectionUtils.toMultiValueMap(Map.of("header", List.of("value")));
        var requestEntity = new HttpEntity<>(requestBody, requestHeadersMultiValueMap);
        var e = new ResourceAccessException("I/O error");
        var apiCallResponse = new ApiCallResponse();

        apiCallRequest.setMethod(requestMethod);
        apiCallRequest.setUri(requestUri);
        apiCallRequest.setParams(requestParams);
        apiCallRequest.setHeaders(requestHeaders);
        apiCallRequest.setBody(requestBody);

        apiCallResponse.setBody(e.toString());

        when(urlBuilder.buildUrl(apiCallRequest)).thenReturn(requestUrl);
        when(headersBuilder.buildHeaders(apiCallRequest)).thenReturn(requestHeadersMultiValueMap);
        when(restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, String.class)).thenThrow(e);
        when(apiCallResponseBuilder.buildApiCallResponse(e)).thenReturn(apiCallResponse);

        var actualResponse = apiCallService.makeCall(apiCallRequest);

        verify(urlBuilder).buildUrl(apiCallRequest);
        verify(headersBuilder).buildHeaders(apiCallRequest);
        verify(restTemplate).exchange(requestUrl, HttpMethod.POST, requestEntity, String.class);
        verify(apiCallResponseBuilder).buildApiCallResponse(e);
        assertEquals(apiCallResponse, actualResponse);
    }
}
