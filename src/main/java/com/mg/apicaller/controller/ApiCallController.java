package com.mg.apicaller.controller;

import com.mg.apicaller.dto.ApiCallRequest;
import com.mg.apicaller.dto.ApiCallResponse;
import com.mg.apicaller.service.ApiCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiCallController {
    private final ApiCallService apiCallService;

    @PostMapping("/api-calls")
    public ApiCallResponse makeCall(@RequestBody ApiCallRequest apiCallRequest) {
        return apiCallService.makeCall(apiCallRequest);
    }
}
