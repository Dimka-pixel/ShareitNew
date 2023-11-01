package com.example.Shareit.User;
//Pull requests
import org.springframework.http.HttpStatus;

public class UserValidateException extends RuntimeException {

    private String errorMessage;
    private HttpStatus status;

    public UserValidateException(String errorMessage, HttpStatus status) {
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

