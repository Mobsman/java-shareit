package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.shareit.booking.bookingRequest.BookingRequest;
import ru.practicum.shareit.booking.bookingRequest.Status;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public Object addBooking(long userId, BookingRequest bookingRequest) {
        return post("", userId, bookingRequest);
    }

    public Object getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public Object approveOrRejectBooking(long bookingId, Boolean isApproved, long userId) {
        Map<String, Object> parameters = Map.of(
                "approved", Boolean.toString(isApproved)
        );
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }


    public Object getBookings(long userId, Status state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get(UriComponentsBuilder.fromPath("")
                .queryParam("state", "{state}")
                .queryParam("from", "{from}")
                .queryParam("size", "{size}")
                .encode()
                .toUriString(), userId, parameters);
    }

    public Object getBookingsOfOwner(long userId, Status state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get(UriComponentsBuilder.fromPath("/owner")
                .queryParam("state", "{state}")
                .queryParam("from", "{from}")
                .queryParam("size", "{size}")
                .encode()
                .toUriString(), userId, parameters);
    }
}
