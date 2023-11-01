package com.example.Shareit.Exception;
//Pull requests
import com.example.Shareit.Booking.BookingValidateException;
import com.example.Shareit.Item.ItemValidateException;
import com.example.Shareit.User.UserValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class AllExceptionHandler {

    @ExceptionHandler(UserValidateException.class)
    protected ResponseEntity<Object> handleConflict(UserValidateException ex, WebRequest request) {
        log.error("UserValidateException: {}", ex.getErrorMessage());
        return ResponseEntity
                .status(ex.getStatus())
                .body(Map.of("errorMessage", ex.getErrorMessage()));
    }

    @ExceptionHandler(ItemValidateException.class)
    protected ResponseEntity<Object> handleConflict(ItemValidateException ex, WebRequest request) {
        log.error("ItemValidateException: {}", ex.getErrorMessage());
        return ResponseEntity
                .status(ex.getStatus())
                .body(Map.of("errorMessage", ex.getErrorMessage()));
    }

    @ExceptionHandler(BookingValidateException.class)
    protected ResponseEntity<Object> handleConflict(BookingValidateException ex, WebRequest request) {
        log.error("BookingValidateException: {}", ex.getErrorMessage());
        return ResponseEntity
                .status(ex.getStatus())
                .body(Map.of("errorMessage", ex.getErrorMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleConflict(EntityNotFoundException ex, WebRequest request) {
        log.error("EntityNotFoundException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("errorMessage", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleConflict(IllegalArgumentException ex, WebRequest request) {
        log.error("IllegalArgumentException");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errorMessage", "Unknown state: UNSUPPORTED_STATUS"));
    }

}
