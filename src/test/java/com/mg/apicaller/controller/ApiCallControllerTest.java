package com.mg.apicaller.controller;

import com.mg.apicaller.dto.ApiCallRequest;
import com.mg.apicaller.dto.ApiCallResponse;
import com.mg.apicaller.service.ApiCallService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiCallControllerTest {
    @InjectMocks
    ApiCallController apiCallController;

    @Mock
    ApiCallService apiCallService;

    @Test
    void makeCall() {
        ApiCallRequest apiCallRequest = new ApiCallRequest();
        ApiCallResponse apiCallResponse = new ApiCallResponse();
        when(apiCallService.makeCall(apiCallRequest)).thenReturn(apiCallResponse);
        ApiCallResponse actualResponse = apiCallController.makeCall(apiCallRequest);
        verify(apiCallService).makeCall(apiCallRequest);
        assertEquals(apiCallResponse, actualResponse);
    }
}
