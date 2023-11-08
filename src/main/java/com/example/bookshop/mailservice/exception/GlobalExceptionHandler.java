package com.example.bookshop.mailservice.exception;

import com.example.bookshop.mailservice.dto.ErrorResponseDto;
import com.example.bookshop.mailservice.mapper.CommonMapper;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MessagingException.class)
    public void handleMessagingException(MessagingException e) {
        e.printStackTrace();
    }

    @ExceptionHandler(ConfirmationCodeExpiredException.class)
    public ResponseEntity<ErrorResponseDto> handleAccountNotFoundException(ConfirmationCodeExpiredException exception, WebRequest request){
        return new ResponseEntity<>(CommonMapper.buildErrorResponse(exception, request, null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidBodyException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidBodyException(InvalidBodyException e, WebRequest request) {
        return new ResponseEntity<>(CommonMapper.buildErrorResponse(e, request, null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(KafkaException.class)
    public ResponseEntity<ErrorResponseDto> handleKafkaException(KafkaException e, WebRequest request) {
        return new ResponseEntity<>(CommonMapper.buildErrorResponse(e, request, null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldName = ((FieldError) objectError).getField();
            String message = objectError.getDefaultMessage();
            errors.put(fieldName, message);
        });
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .apiPath(request.getDescription(false))
                .message("Thông tin nhập không hợp lệ")
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST)
                .errors(errors)
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }
}
