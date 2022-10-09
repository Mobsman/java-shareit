package ru.practicum.shareit.booking.exception;

public class BookingStateException extends RuntimeException {
    public BookingStateException(String state) {
        super("Unknown state: " + state);
    }
}
