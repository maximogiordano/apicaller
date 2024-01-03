package com.mg.apicaller.builder;

import com.mg.apicaller.dto.ApiCallResponse;
import com.mg.apicaller.dto.KeyValuePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiCallResponseBuilderTest {
    @InjectMocks
    ApiCallResponseBuilder apiCallResponseBuilder;

    @Mock
    ApiCallResponseHeadersBuilder apiCallResponseHeadersBuilder;

    @Test
    void testSuccessfulResponse() {
        int status = 200;
        HttpHeaders httpHeaders = new HttpHeaders(CollectionUtils.toMultiValueMap(Map.of("Content-Type",
                List.of("application/json"))));
        String body = "[1,2,3]";
        ResponseEntity<String> response = ResponseEntity.status(status).headers(httpHeaders).body(body);

        ApiCallResponse expected = new ApiCallResponse();
        expected.setStatus(status);
        List<KeyValuePair> headers = List.of(new KeyValuePair("Content-Type", "application/json"));
        expected.setHeaders(headers);
        expected.setBody(body);

        when(apiCallResponseHeadersBuilder.buildApiCallResponseHeaders(httpHeaders)).thenReturn(headers);
        ApiCallResponse actualResponse = apiCallResponseBuilder.buildApiCallResponse(response);
        verify(apiCallResponseHeadersBuilder).buildApiCallResponseHeaders(httpHeaders);
        assertEquals(expected, actualResponse);
    }

    @Test
    void testUnsuccessfulResponse() {
        int status = 404;
        HttpHeaders httpHeaders = new HttpHeaders(CollectionUtils.toMultiValueMap(Map.of("Content-Type",
                List.of("application/json"))));
        String body = "{\"message\":\"could not find resource\"}";
        HttpStatusCodeException httpStatusCodeException = mock(HttpStatusCodeException.class);
        when(httpStatusCodeException.getStatusCode()).thenReturn(HttpStatusCode.valueOf(status));
        when(httpStatusCodeException.getResponseHeaders()).thenReturn(httpHeaders);
        when(httpStatusCodeException.getResponseBodyAsString()).thenReturn(body);

        ApiCallResponse expected = new ApiCallResponse();
        expected.setStatus(status);
        List<KeyValuePair> headers = List.of(new KeyValuePair("Content-Type", "application/json"));
        expected.setHeaders(headers);
        expected.setBody(body);

        when(apiCallResponseHeadersBuilder.buildApiCallResponseHeaders(httpHeaders)).thenReturn(headers);
        ApiCallResponse actualResponse = apiCallResponseBuilder.buildApiCallResponse(httpStatusCodeException);
        verify(apiCallResponseHeadersBuilder).buildApiCallResponseHeaders(httpHeaders);
        verify(httpStatusCodeException).getStatusCode();
        verify(httpStatusCodeException).getResponseHeaders();
        verify(httpStatusCodeException).getResponseBodyAsString();
        assertEquals(expected, actualResponse);
    }

    @Test
    void testErrorResponse() {
        Exception e = new IOException("oops!");
        ApiCallResponse expected = new ApiCallResponse();
        expected.setBody(e.toString());
        ApiCallResponse actualResponse = apiCallResponseBuilder.buildApiCallResponse(e);
        assertEquals(expected, actualResponse);
    }
}
