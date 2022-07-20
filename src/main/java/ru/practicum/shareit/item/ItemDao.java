package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import java.util.Collection;


public interface ItemDao {

    Item create(Item item);

    Item update(Item item,Long id);

    Item getById(Long id);

    Collection<Item> getAllItems(Long userId);

    Collection<Item> searchItemByName(String itemName);
}
