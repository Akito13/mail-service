package com.example.bookshop.mailservice.mapper;

import com.example.bookshop.mailservice.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;

public class CommonMapper {
    public static ErrorResponseDto buildErrorResponse(RuntimeException exception, WebRequest request, Map<String, String> errors, HttpStatus httpStatus){
        return ErrorResponseDto.builder()
                .apiPath(request.getDescription(false))
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .statusCode(httpStatus)
                .errors(errors)
                .build();
    }
}
