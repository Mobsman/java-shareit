package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@NotEmpty @RequestHeader(USER_ID_HEADER) Long id,
                                            @Valid @RequestBody ItemRequest itemRequest) {

        return itemRequestService.createRequest(itemRequest, id);

    }

    @GetMapping
    public List<ItemRequestDto> getItemRequest(@NotEmpty @RequestHeader(USER_ID_HEADER) Long id) {

        return itemRequestService.getRequest(id);

    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(USER_ID_HEADER) Long id,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {

        return itemRequestService.getAllRequest(id, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader(USER_ID_HEADER) Long id,
                                         @PathVariable Long requestId) {

        return itemRequestService.geyRequestById(requestId, id);
    }

}
