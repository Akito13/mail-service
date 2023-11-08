package com.example.bookshop.mailservice.exception;

public class InvalidBodyException extends RuntimeException{
    public InvalidBodyException(String message) {
        super(message);
    }
}
