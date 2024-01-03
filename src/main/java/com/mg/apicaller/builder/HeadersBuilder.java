package com.mg.apicaller.builder;

import com.mg.apicaller.dto.ApiCallRequest;
import com.mg.apicaller.dto.KeyValuePair;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.toMultiValueMap;

@Component
public class HeadersBuilder {
    public MultiValueMap<String, String> buildHeaders(ApiCallRequest apiCallRequest) {
        return toMultiValueMap(toMap(apiCallRequest.getHeaders()));
    }

    private Map<String, List<String>> toMap(List<KeyValuePair> list) {
        if (list == null) {
            return Map.of();
        }

        return list.stream().collect(groupingBy(KeyValuePair::getKey, mapping(KeyValuePair::getValue, toList())));
    }
}
