package com.example.ringleassignment.common.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum StatusCode {
    OK(200, "OK",HttpStatus.OK);
    @Getter
    private int statusCode;
    @Getter
    private String message;
    @Getter
    private HttpStatus status;

    StatusCode(int statusCode, String message, HttpStatus status) {
        this.statusCode = statusCode;
        this.message = message;
        this.status = status;
    }

    public String toString() {
        return "{ " +
                "\n\"code\" : " + "\""+ statusCode +"\"" +
                "\n\"status\" : " + "\""+status+"\"" +
                "\n\"message\" : " + "\""+message+"\"" +
                "\n}";
    }
}
