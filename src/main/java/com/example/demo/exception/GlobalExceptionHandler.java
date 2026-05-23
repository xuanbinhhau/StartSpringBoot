package com.example.demo.exception;

import com.example.demo.dto.response.ApiResponse;
import org.springframework.boot.web.error.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        String enumKey = ex.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try{
            errorCode = ErrorCode.valueOf(enumKey);
        }catch (IllegalArgumentException e){

        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDenied(AccessDeniedException exception){
        return ResponseEntity.status(ErrorCode.UN_AUTHORIZED.getStatusCode()).body(
                ApiResponse.builder()
                        .code(ErrorCode.UN_AUTHORIZED.getCode())
                        .message(ErrorCode.UN_AUTHORIZED.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException ex){
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setResult((errorCode.getMessage()));
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingAllExcepion(Exception ex){

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCASEGOIZE.getCode());
        apiResponse.setMessage(ErrorCode.UNCASEGOIZE.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

//    @ExceptionHandler(value = IllegalArgumentException.class)
//    ResponseEntity<ApiResponse> handlingIllagalArgumentException(Exception ex){
//
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
//        apiResponse.setMessage(ErrorCode.INVALID_KEY.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//    }

}
