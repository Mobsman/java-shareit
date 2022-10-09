package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(BookingStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConflict(BookingStateException ex) {
        return Map.of("error", ex.getMessage());
    }
}
