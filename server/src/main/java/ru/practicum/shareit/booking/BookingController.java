package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {


    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;


    @PostMapping
    public BookingDto addBooking(@RequestHeader(USER_ID_HEADER) long bookerId,
                                 @Valid @RequestBody BookingRequest booking) {

        return bookingService.addBooking(bookerId, booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveOrRejectBooking(@PathVariable long bookingId,
                                             @RequestParam("approved") Boolean isApproved,
                                             @RequestHeader(USER_ID_HEADER) long ownerId) {
        return bookingService.approveOrRejectBooking(bookingId, isApproved, ownerId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(USER_ID_HEADER) long userId,
                                     @PathVariable long bookingId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingOfCurrentUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(defaultValue = "ALL") Status state,
                                                       @Valid @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        if (from < 0) {
            throw new IllegalArgumentException();
        }
        return bookingService.getAllBookingOfCurrentUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllItemBookingsOfCurrentUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                            @RequestParam(defaultValue = "ALL") Status state,
                                                            @Valid @RequestParam(defaultValue = "0") Integer from,
                                                            @RequestParam(defaultValue = "10") Integer size) {
        if (from < 0) {
            throw new IllegalArgumentException();
        }
        return bookingService.getAllItemBookingsOfCurrentUser(userId, state, from, size);
    }
}
