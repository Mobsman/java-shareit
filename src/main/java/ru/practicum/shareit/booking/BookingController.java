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
    public List<BookingDto> getAllBookingOfCurrentUser(@RequestParam(name = "state", defaultValue = "ALL")
                                                               Status state,
                                                       @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.getAllBookingOfCurrentUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllItemBookingsOfCurrentUser(@RequestParam(name = "state", defaultValue = "ALL")
                                                                    Status state,
                                                            @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.getAllItemBookingsOfCurrentUser(userId, state);
    }
}
