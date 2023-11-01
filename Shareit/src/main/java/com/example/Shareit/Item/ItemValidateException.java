package com.example.Shareit.Item;
//Pull requests
import org.springframework.http.HttpStatus;

public class ItemValidateException extends RuntimeException {

    private String errorMessage;
    private HttpStatus status;

    public ItemValidateException() {
    }

    public ItemValidateException(String errorMessage, HttpStatus status) {
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
