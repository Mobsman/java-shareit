package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.requst.ItemRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {


    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final RequestClient requestClient;

    @PostMapping
    public Object createItemRequest(@RequestHeader(USER_ID_HEADER) long userId,
                                    @RequestBody @Valid ItemRequest itemRequest) {

        log.info("Creating items " + itemRequest.toString() + " by user with id " + userId);
        return requestClient.createItemRequest(userId, itemRequest);
    }

    @GetMapping
    public Object getAllItemRequestsByUserId(@RequestHeader(USER_ID_HEADER) long userId) {

        log.info("Get item requests of userId={}", userId);
        return requestClient.getAllItemRequestsOfUser(userId);
    }

    @GetMapping("/{requestId}")
    public Object getItemRequestById(@RequestHeader(USER_ID_HEADER) long userId,
                                     @PathVariable long requestId) {

        log.info("Get itemRequestId={}, userId={}", requestId, userId);
        return requestClient.getItemRequestById(requestId, userId);
    }

    @GetMapping("/all")
    public Object getAllItemRequestsPaginated(@RequestHeader(USER_ID_HEADER) long userId,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Get item requests, userId={}, from={}, size={}", userId, from, size);
        return requestClient.getAllItemRequests(userId, from, size);
    }

}
