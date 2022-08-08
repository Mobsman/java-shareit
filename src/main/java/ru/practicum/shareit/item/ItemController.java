package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;


@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService service;

    @PostMapping
    public ItemDto createItem(@NotEmpty @RequestHeader(USER_ID_HEADER) Long id,
                              @Valid @RequestBody Item item) throws UserNotFoundException {

        return service.create(id, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@NotEmpty @RequestHeader(USER_ID_HEADER) Long ownerId,
                              @RequestBody Item item,
                              @PathVariable Long itemId) {
        return service.update(ownerId, item, itemId);
    }


    @GetMapping("/{itemId}")
    public ItemDto getItemById(@NotEmpty @RequestHeader(USER_ID_HEADER) long userId,
                               @PathVariable long itemId) {
        return service.getById(itemId,userId);
    }

    @GetMapping
    public Collection<ItemDto> getAllItemsByUserId(@NotEmpty @RequestHeader(USER_ID_HEADER) long id) {
        return service.getAllItemsByUserId(id);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestParam String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return service.searchItemByName(text);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@NotEmpty @RequestHeader(USER_ID_HEADER) long userId,
                                  @PathVariable long itemId,
                                  @RequestBody Comment comment) {
        return service.addComment(userId, itemId, comment);
    }

}
