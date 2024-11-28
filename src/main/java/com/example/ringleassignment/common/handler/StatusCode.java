package com.example.ringleassignment.common.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StatusCode {
    OK(200, "OK",HttpStatus.OK),
    NOT_FOUND(404,"회원 정보가 잘못되었습니다.",HttpStatus.NOT_FOUND),
    NOT_EXIST(400, "존재하지 않는 데이터입니다.", HttpStatus.BAD_REQUEST),
    MALFORMED(400, "형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    CONFLICT(409, "동시성 충돌이 발생했습니다. 다시 시도해주세요.", HttpStatus.CONFLICT),
    FORBIDDEN(403, "해당 요청의 권한이 없습니다.", HttpStatus.FORBIDDEN);


    private int statusCode;
    private String message;
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
