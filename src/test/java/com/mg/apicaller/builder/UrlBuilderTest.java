package com.mg.apicaller.builder;

import com.mg.apicaller.dto.ApiCallRequest;
import com.mg.apicaller.dto.KeyValuePair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlBuilderTest {
    @ParameterizedTest
    @MethodSource
    void buildUrl(ApiCallRequest apiCallRequest, String expected) {
        assertEquals(expected, new UrlBuilder().buildUrl(apiCallRequest));
    }

    static Stream<Arguments> buildUrl() {
        return Stream.of(
                Arguments.of(NO_PARAMS, "http://no-params.com"),
                Arguments.of(ONE_PARAM, "http://one-param.com?param1=value1"),
                Arguments.of(TWO_PARAMS, "http://two-params.com?param1=value1&param2=value2"),
                Arguments.of(THREE_PARAMS, "http://three-params.com?param1=value1&param2=value2&param3=value3"),
                Arguments.of(SPECIAL_CHARS, "http://special-chars.com?param=The+string+%C3%BC%40foo-bar")
        );
    }

    static final ApiCallRequest NO_PARAMS = buildApiCallRequest("http://no-params.com", List.of());

    static final ApiCallRequest ONE_PARAM = buildApiCallRequest("http://one-param.com", List.of(
            new KeyValuePair("param1", "value1")
    ));

    static final ApiCallRequest TWO_PARAMS = buildApiCallRequest("http://two-params.com", List.of(
            new KeyValuePair("param1", "value1"),
            new KeyValuePair("param2", "value2")
    ));

    static final ApiCallRequest THREE_PARAMS = buildApiCallRequest("http://three-params.com", List.of(
            new KeyValuePair("param1", "value1"),
            new KeyValuePair("param2", "value2"),
            new KeyValuePair("param3", "value3")
    ));

    static final ApiCallRequest SPECIAL_CHARS = buildApiCallRequest("http://special-chars.com", List.of(
            new KeyValuePair("param", "The string ü@foo-bar")
    ));

    static ApiCallRequest buildApiCallRequest(String uri, List<KeyValuePair> params) {
        ApiCallRequest apiCallRequest = new ApiCallRequest();

        apiCallRequest.setUri(uri);
        apiCallRequest.setParams(params);

        return apiCallRequest;
    }
}
