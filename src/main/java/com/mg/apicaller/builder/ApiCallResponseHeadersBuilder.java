package com.mg.apicaller.builder;

import com.mg.apicaller.dto.KeyValuePair;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class ApiCallResponseHeadersBuilder {
    public List<KeyValuePair> buildApiCallResponseHeaders(HttpHeaders headers) {
        if (headers == null) {
            return Collections.emptyList();
        }

        return headers.entrySet()
                .stream()
                .flatMap(this::getKeyValuePairStream)
                .toList();
    }

    private Stream<KeyValuePair> getKeyValuePairStream(Map.Entry<String, List<String>> entry) {
        return entry.getValue()
                .stream()
                .map(value -> new KeyValuePair(entry.getKey(), value));
    }
}
