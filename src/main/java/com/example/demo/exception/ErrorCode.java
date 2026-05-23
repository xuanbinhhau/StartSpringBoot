package com.example.demo.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCASEGOIZE(9999, "UNCAGOGIZE EXCEPTION", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key",HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User alreadly exists",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "User Name Must Be At Least 3 character",HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at leat 8 character",HttpStatus.BAD_REQUEST),
    USER_NOT_EXITED(1005, "User not exists",HttpStatus.NOT_FOUND),
    UN_AUTHENTICATED(1006, "Unauthenticated",HttpStatus.UNAUTHORIZED),
    UN_AUTHORIZED(1007, "You do not have permitsion",HttpStatus.FORBIDDEN),
    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}