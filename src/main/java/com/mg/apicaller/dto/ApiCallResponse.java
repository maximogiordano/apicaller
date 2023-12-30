package com.mg.apicaller.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiCallResponse {
    private Integer status;
    private List<KeyValuePair> headers;
    private String body;
}
