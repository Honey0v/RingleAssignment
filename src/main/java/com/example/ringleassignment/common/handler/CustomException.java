package com.example.ringleassignment.common.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{
    StatusCode authCode;
}