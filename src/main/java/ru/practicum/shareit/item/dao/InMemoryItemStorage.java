package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class InMemoryItemStorage implements ItemDao {

    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item create(Item item) {

        item.setId(getId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item, Long id) {

        Item oldItem = getById(id);

        if (oldItem != null) {
            Item update = new Item();
            update.setId(id);
            update.setName(item.getName() == null ? oldItem.getName() : item.getName());
            update.setDescription(item.getDescription() == null ? oldItem.getDescription() : item.getDescription());
            update.setOwner(item.getOwner());
            update.setAvailable(item.getAvailable() == null ? oldItem.getAvailable() : item.getAvailable());
            items.put(oldItem.getId(), update);
            return update;
        }
        return null;
    }


    @Override
    public Item getById(Long id) {
        return items.get(id);
    }


    @Override
    public Collection<Item> getAllItems(Long userId) {

        return items.values().stream().filter(i -> i.getOwner().getId().equals(userId)).collect(Collectors.toList());

    }

    @Override
    public Collection<Item> searchItemByName(String itemName) {

        Stream<Item> itemN = items.values().stream().filter(i -> (i.getName().toLowerCase()).contains(itemName.toLowerCase()))
                .filter(Item::getAvailable);
        Stream<Item> itemD = items.values().stream().filter(i -> (i.getDescription().toLowerCase()).contains(itemName.toLowerCase()))
                .filter(Item::getAvailable);
        return Stream.concat(itemN, itemD).distinct().collect(Collectors.toList());
    }

    private Long getId() {
        id++;
        return id;
    }
}
