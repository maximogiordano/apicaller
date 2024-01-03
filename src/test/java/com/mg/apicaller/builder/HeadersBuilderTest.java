package com.mg.apicaller.builder;

import com.mg.apicaller.dto.ApiCallRequest;
import com.mg.apicaller.dto.KeyValuePair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeadersBuilderTest {
    @ParameterizedTest
    @MethodSource
    void buildHeaders(ApiCallRequest apiCallRequest, MultiValueMap<String, String> expected) {
        assertEquals(expected, new HeadersBuilder().buildHeaders(apiCallRequest));
    }

    static Stream<Arguments> buildHeaders() {
        return Stream.of(
                Arguments.of(NULL_INPUT, NULL_OUTPUT),
                Arguments.of(EMPTY_INPUT, EMPTY_OUTPUT),
                Arguments.of(ONE_HEADER_INPUT, ONE_HEADER_OUTPUT),
                Arguments.of(TWO_HEADERS_INPUT, TWO_HEADERS_OUTPUT),
                Arguments.of(MULTI_HEADER_INPUT, MULTI_HEADER_OUTPUT)
        );
    }

    static final ApiCallRequest NULL_INPUT = buildApiCallRequest(null);

    static final MultiValueMap<String, String> NULL_OUTPUT = CollectionUtils.toMultiValueMap(Map.of());

    static final ApiCallRequest EMPTY_INPUT = buildApiCallRequest(List.of());

    static final MultiValueMap<String, String> EMPTY_OUTPUT = CollectionUtils.toMultiValueMap(Map.of());

    static final ApiCallRequest ONE_HEADER_INPUT = buildApiCallRequest(List.of(
            new KeyValuePair("header", "value")
    ));
    static final MultiValueMap<String, String> ONE_HEADER_OUTPUT = CollectionUtils.toMultiValueMap(Map.of(
            "header", List.of("value")
    ));

    static final ApiCallRequest TWO_HEADERS_INPUT = buildApiCallRequest(List.of(
            new KeyValuePair("header-1", "value-1"),
            new KeyValuePair("header-2", "value-2")
    ));

    static final MultiValueMap<String, String> TWO_HEADERS_OUTPUT = CollectionUtils.toMultiValueMap(Map.of(
            "header-1", List.of("value-1"),
            "header-2", List.of("value-2")
    ));

    static final ApiCallRequest MULTI_HEADER_INPUT = buildApiCallRequest(List.of(
            new KeyValuePair("header-1", "value-1a"),
            new KeyValuePair("header-1", "value-1b"),
            new KeyValuePair("header-2", "value-2")
    ));

    static final MultiValueMap<String, String> MULTI_HEADER_OUTPUT = CollectionUtils.toMultiValueMap(Map.of(
            "header-1", List.of("value-1a", "value-1b"),
            "header-2", List.of("value-2")
    ));

    static ApiCallRequest buildApiCallRequest(List<KeyValuePair> headers) {
        ApiCallRequest apiCallRequest = new ApiCallRequest();
        apiCallRequest.setHeaders(headers);
        return apiCallRequest;
    }
}
