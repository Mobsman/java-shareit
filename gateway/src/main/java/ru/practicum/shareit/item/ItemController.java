package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.itemRequest.CommentRequest;
import ru.practicum.shareit.item.itemRequest.ItemCreateRequest;
import ru.practicum.shareit.item.itemRequest.ItemUpdateRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;


    @PostMapping
    public Object createItem(@RequestBody @Valid ItemCreateRequest request,
                             @RequestHeader(USER_ID_HEADER) @Positive Long userOwner) {

        return itemClient.createItem(request, userOwner);
    }

    @PatchMapping("/{itemId}")
    public Object updateItem(@RequestBody @Valid ItemUpdateRequest request,
                             @PathVariable("itemId") @Positive Long itemId,
                             @RequestHeader(USER_ID_HEADER) @Positive Long userOwner) {


        return itemClient.updateItem(request, itemId, userOwner);
    }

    @GetMapping("/{itemId}")
    public Object getItemById(@PathVariable("itemId") @Positive Long itemId,
                              @RequestHeader(USER_ID_HEADER) @Positive Long userOwner) {

        return itemClient.getItemById(itemId, userOwner);
    }

    @GetMapping
    public Object getAllItemsByUserId(@RequestHeader(USER_ID_HEADER) long userOwner,
                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                      @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        return itemClient.getAllItems(userOwner, from, size);
    }

    @GetMapping("/search")
    public Object searchItems(@RequestHeader(USER_ID_HEADER) long userOwner,
                              @RequestParam String text,
                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        return itemClient.search(userOwner, text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public Object postComment(@RequestHeader(USER_ID_HEADER) long userId,
                              @PathVariable long itemId,
                              @RequestBody @Valid CommentRequest commentRequest) {

        return itemClient.addComment(userId, commentRequest, itemId);
    }

}
