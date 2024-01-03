package com.mg.apicaller.builder;

import com.mg.apicaller.dto.KeyValuePair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiCallResponseHeadersBuilderTest {
    @ParameterizedTest
    @MethodSource
    void buildApiCallResponseHeaders(HttpHeaders headers, List<KeyValuePair> expected) {
        assertEquals(expected, new ApiCallResponseHeadersBuilder().buildApiCallResponseHeaders(headers));
    }

    static Stream<Arguments> buildApiCallResponseHeaders() {
        return Stream.of(
                Arguments.of(null, List.of()),
                Arguments.of(new HttpHeaders(), List.of()),
                Arguments.of(buildHttpHeaders(ONE_HEADER_LIST), ONE_HEADER_LIST),
                Arguments.of(buildHttpHeaders(TWO_HEADERS_LIST), TWO_HEADERS_LIST),
                Arguments.of(buildHttpHeaders(MULTI_HEADER_LIST), MULTI_HEADER_LIST)
        );
    }

    static final List<KeyValuePair> ONE_HEADER_LIST = List.of(
            new KeyValuePair("header", "value")
    );

    static final List<KeyValuePair> TWO_HEADERS_LIST = List.of(
            new KeyValuePair("header-1", "value-1"),
            new KeyValuePair("header-2", "value-2")
    );

    static final List<KeyValuePair> MULTI_HEADER_LIST = List.of(
            new KeyValuePair("header-1", "value-1a"),
            new KeyValuePair("header-1", "value-1b"),
            new KeyValuePair("header-2", "value-2")
    );

    static HttpHeaders buildHttpHeaders(List<KeyValuePair> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach(header -> httpHeaders.add(header.getKey(), header.getValue()));
        return httpHeaders;
    }
}
