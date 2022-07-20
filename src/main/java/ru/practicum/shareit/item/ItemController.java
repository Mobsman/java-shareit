package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
    public ItemDto updateItem(@NotEmpty @RequestHeader(USER_ID_HEADER) Long id,
                              @RequestBody Item item,
                              @PathVariable Long itemId) {
        return service.update(id, item, itemId);
    }


    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return service.getById(itemId);
    }

    @GetMapping
    public Collection<ItemDto> getAllItemsByUserId(@NotEmpty @RequestHeader(USER_ID_HEADER) long id) {
        return service.getAllItemsByUserId(id);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestParam String text) {
        if (text == null || text.isEmpty()){
            return new ArrayList<>();
        }
        return service.searchItemByName(text);
    }

}
