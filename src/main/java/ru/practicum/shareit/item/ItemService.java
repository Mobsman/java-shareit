package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ConverterItemToDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDao;
import ru.practicum.shareit.user.UserNotFoundException;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ItemService {

    private final ItemDao itemDao;
    private final UserDao userDao;
    private ConverterItemToDto converter;


    public ItemDto create(Long ownerId, Item item) {
        User user = userDao.getUserById(ownerId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        item.setOwner(user);
        Item itm = itemDao.create(item);
        return converter.convert(itemDao.getById(itm.getId()));

    }

    public ItemDto update(Long ownerId, Item item, Long itemId) {
        User user = userDao.getUserById(ownerId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        Item i = itemDao.getById(itemId);

        if (i == null || !(i.getOwner().getId().equals(user.getId()))) {
            throw new ItemNotFoundException("Предмет не найден");
        }

        item.setOwner(user);
        Item itm = itemDao.update(item, itemId);
        return converter.convert(itm);

    }

    public ItemDto getById(Long itemId) {
        return converter.convert(itemDao.getById(itemId));
    }

    public Collection<ItemDto> getAllItemsByUserId(Long ownerId) {

        User user = userDao.getUserById(ownerId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        Collection<Item> items = itemDao.getAllItems(ownerId);
        return items.stream().map(converter::convert).collect(Collectors.toList());

    }

    public Collection<ItemDto> searchItemByName(String itemName) {

        Collection<Item> items = itemDao.searchItemByName(itemName);
        return items.stream().map(converter::convert).collect(Collectors.toList());

    }


}
