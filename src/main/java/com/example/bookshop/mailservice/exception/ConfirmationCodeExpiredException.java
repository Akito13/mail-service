package com.example.bookshop.mailservice.exception;

public class ConfirmationCodeExpiredException extends RuntimeException{
    public ConfirmationCodeExpiredException(String message) {
        super(message);
    }
}
