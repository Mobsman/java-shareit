package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ConverterCommentItemToCommentDto;
import ru.practicum.shareit.item.dto.ConverterItemToDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ConverterItemToDto converter;
    private final ConverterCommentItemToCommentDto commentConverter;


    public ItemDto create(Long ownerId, Item item) {
        Optional<User> user = userRepository.findById(ownerId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        item.setOwner(user.get());
        Item itm = itemRepository.save(item);
        return converter.convert(itemRepository.getReferenceById(itm.getId()));

    }

    public ItemDto update(Long ownerId, Item item, Long itemId) {

        Optional<User> user = userRepository.findById(ownerId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        Optional<Item> oldItem = itemRepository.findById(itemId);

        if (oldItem.isEmpty() || !(oldItem.get().getOwner().getId().equals(user.get().getId()))) {
            throw new ItemNotFoundException("Предмет не найден");
        }

        if (oldItem.isPresent()) {
            oldItem.get().setName(item.getName() == null ? oldItem.get().getName() : item.getName());
            oldItem.get().setDescription(item.getDescription() == null ? oldItem.get().getDescription() : item.getDescription());
            oldItem.get().setOwner(user.get());
            oldItem.get().setAvailable(item.getAvailable() == null ? oldItem.get().getAvailable() : item.getAvailable());
            Item updateItem = itemRepository.save(oldItem.get());
            return converter.convert(itemRepository.getReferenceById(updateItem.getId()));
        }
        return null;
    }

    public ItemDto getById(Long itemId, Long userId) {

        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new ItemNotFoundException("предмет не найден");
        }
        Booking bookingBefore = bookingRepository.findFirstBookingByItemIdAndEndBeforeOrderByStartAsc(item.get().getId(), LocalDateTime.now());
        Booking bookingAfter = bookingRepository.findFirstBookingByItemIdAndStartAfterOrderByStartAsc(item.get().getId(), LocalDateTime.now());

        if (userId.equals(item.get().getOwner().getId())) {
            return converter.convertBooking(item.get(), bookingBefore, bookingAfter);
        }
        return converter.convert(item.get());
    }

    public Collection<ItemDto> getAllItemsByUserId(Long ownerId) {

        Optional<User> user = userRepository.findById(ownerId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        Collection<Item> items = itemRepository.findItemByOwnerId(ownerId);

        return items.stream().map(item -> converter.convertBooking(item,
                        bookingRepository.findFirstBookingByItemIdAndEndBeforeOrderByStartAsc(item.getId(), LocalDateTime.now()),
                        bookingRepository.findFirstBookingByItemIdAndStartAfterOrderByStartAsc(item.getId(), LocalDateTime.now())))
                .sorted(Comparator.comparingLong(ItemDto::getId))
                .collect(Collectors.toList());

    }

    public Collection<ItemDto> searchItemByName(String itemName) {

        String search = itemName.toLowerCase();

        Collection<Item> items = itemRepository.findItemByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableIsTrue(search, search);
        return items.stream().map(converter::convert).collect(Collectors.toList());

    }

    public CommentDto addComment(long userId, long itemId, CommentRequest comment) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("пользователь не найден"));

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("предмет не найден"));

        List<Booking> booking = bookingRepository.findBookingsByItemIdAndBookerIdAndStatusAndStartBefore(itemId,
                userId, Status.APPROVED, LocalDateTime.now());

        if (booking == null || booking.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "заявка не найдена");
        }
        if (comment.getText().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "комментарий пустой");
        }

        Comment com = new Comment();
        com.setText(comment.getText());
        com.setCreated(LocalDateTime.now());
        com.setItem(item);
        com.setUser(user);

        Comment saveComment = commentRepository.save(com);

        return commentConverter.convert(commentRepository.getReferenceById(saveComment.getId()));
    }

}
