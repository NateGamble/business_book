package com.revature.util;

import java.time.LocalDateTime;

import com.revature.dtos.ErrorResponse;

import org.springframework.http.HttpStatus;

public class ErrorResponseFactory {

    private static ErrorResponseFactory errRespFactory = new ErrorResponseFactory();

    private ErrorResponseFactory() {
        super();
    }

    public static ErrorResponseFactory getInstance() {
        return errRespFactory;
    }

    public ErrorResponse generateErrorResponse(int status, String message) {
        return new ErrorResponse(status, message, LocalDateTime.now());
    }

    public ErrorResponse generateErrorResponse(HttpStatus status) {
        return new ErrorResponse(status.value(), status.toString(), LocalDateTime.now());
    }

}
