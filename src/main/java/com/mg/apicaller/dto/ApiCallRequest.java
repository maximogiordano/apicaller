package com.mg.apicaller.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiCallRequest {
    private String method;
    private String uri;
    private List<KeyValuePair> params;
    private List<KeyValuePair> headers;
    private String body;
}
