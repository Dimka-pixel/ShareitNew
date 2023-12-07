package com.example.Shareit.Booking;

import org.springframework.http.HttpStatus;

public class BookingValidateException extends RuntimeException {
    private String errorMessage;
    private HttpStatus status;

    public BookingValidateException(String errorMessage, HttpStatus status) {
        this.errorMessage = errorMessage;
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
