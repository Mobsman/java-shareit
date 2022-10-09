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
public class BookingController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public Object addBooking(@RequestHeader(USER_ID_HEADER) long userId,
                             @RequestBody @Valid BookingRequest bookingRequest) {

        log.info("Creating booking {}, userId={}", bookingRequest, userId);
        return bookingClient.addBooking(userId, bookingRequest);
    }

    @GetMapping("/{bookingId}")
    public Object getBooking(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable Long bookingId) {

        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public Object approveOrRejectBooking(@PathVariable long bookingId,
                                         @RequestParam("approved") Boolean isApproved,
                                         @RequestHeader(USER_ID_HEADER) long ownerId) {

        log.info("Approve booking {} by userId={}", bookingId, ownerId);
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

        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
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

        log.info("Get bookings by owner userId={}", userId);
        return bookingClient.getBookingsOfOwner(userId, state, from, size);
    }

}