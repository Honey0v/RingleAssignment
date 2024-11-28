package com.example.ringleassignment.common.dto;

import com.example.ringleassignment.common.handler.StatusCode;
import lombok.Data;
import lombok.Getter;

@Getter
public class Message {
    public static final String DEFAULT_RESPONSE = "Request processed successfully";
    private int statusCode;
    private String message;
    private Object data;

    public Message(StatusCode statusCode, Object data) {
        this.statusCode = statusCode.getStatusCode();
        this.message = statusCode.getMessage();
        this.data = data;
    }

    public Message(StatusCode statusCode) {
        this.statusCode = statusCode.getStatusCode();
        this.message = statusCode.getMessage();
        this.data = DEFAULT_RESPONSE;
    }
}