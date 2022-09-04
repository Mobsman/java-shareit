package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.bookingRequest.BookingRequest;
import ru.practicum.shareit.booking.bookingRequest.Status;
import ru.practicum.shareit.booking.exception.BookingStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController  {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public Object addBooking(@RequestHeader(USER_ID_HEADER) long userId,
                             @RequestBody @Valid BookingRequest bookingRequest) {

        return bookingClient.addBooking(userId, bookingRequest);
    }

    @GetMapping("/{bookingId}")
    public Object getBooking(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public Object approveOrRejectBooking(@PathVariable long bookingId,
                                         @RequestParam("approved") Boolean isApproved,
                                         @RequestHeader(USER_ID_HEADER) long ownerId) {
        return bookingClient.approveOrRejectBooking(bookingId, isApproved, ownerId);
    }

    @GetMapping
    public Object getBookings(@RequestHeader(USER_ID_HEADER) long userId,
                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Status state = Status
                .from(stateParam)
                .orElseThrow(() -> new BookingStateException(stateParam));

        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public Object getBookingsOfOwner(@RequestHeader(USER_ID_HEADER) long userId,
                                     @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Status state = Status
                .from(stateParam)
                .orElseThrow(() -> new BookingStateException(stateParam));

        return bookingClient.getBookingsOfOwner(userId, state, from, size);
    }

}