package com.mg.apicaller.builder;

import com.mg.apicaller.dto.ApiCallRequest;
import com.mg.apicaller.dto.KeyValuePair;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.Charset;

import static java.util.stream.Collectors.joining;

@Component
public class UrlBuilder {
    public String buildUrl(ApiCallRequest apiCallRequest) {
        String url = apiCallRequest.getUri();

        if (apiCallRequest.getParams() != null && !apiCallRequest.getParams().isEmpty()) {
            url += "?" + apiCallRequest.getParams().stream().map(this::toParam).collect(joining("&"));
        }

        return url;
    }

    private String toParam(KeyValuePair keyValuePair) {
        return encode(keyValuePair.getKey()) + "=" + encode(keyValuePair.getValue());
    }

    private String encode(String s) {
        return URLEncoder.encode(s, Charset.defaultCharset());
    }
}
