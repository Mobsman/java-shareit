package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private final ItemRequestRepository itemRequestRepository;
    private final ConverterItemToDto converter;
    private final ConverterCommentItemToCommentDto commentConverter;


    public ItemDto create(Long ownerId, Item item) {

        User user = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException("пользователь не найден"));

        if (item.getRequestId() != null) {
            Optional<ItemRequest> request = itemRequestRepository.findById(item.getRequestId());
            item.setRequest(request.orElse(null));
        }
        item.setOwner(user);
        Item itm = itemRepository.save(item);
        return converter.convert(itemRepository.getReferenceById(itm.getId()));

    }

    public ItemDto update(Long ownerId, Item item, Long itemId) {

        User user = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException("пользователь не найден"));

        Optional<Item> oldItem = itemRepository.findById(itemId);

        if (oldItem.isEmpty() || !(oldItem.get().getOwner().getId().equals(user.getId()))) {
            throw new ItemNotFoundException("Предмет не найден");
        }

        oldItem.get().setName(item.getName() == null ? oldItem.get().getName() : item.getName());
        oldItem.get().setDescription(item.getDescription() == null ? oldItem.get().getDescription() : item.getDescription());
        oldItem.get().setOwner(user);
        oldItem.get().setAvailable(item.getAvailable() == null ? oldItem.get().getAvailable() : item.getAvailable());
        Item updateItem = itemRepository.save(oldItem.get());
        return converter.convert(itemRepository.getReferenceById(updateItem.getId()));
    }

    public ItemDto getById(Long itemId, Long userId) {

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("предмет не найден"));

        Booking bookingBefore = bookingRepository.findFirstBookingByItemIdAndEndBeforeOrderByStartAsc(item.getId(), LocalDateTime.now());
        Booking bookingAfter = bookingRepository.findFirstBookingByItemIdAndStartAfterOrderByStartAsc(item.getId(), LocalDateTime.now());

        if (userId.equals(item.getOwner().getId())) {
            return converter.convertBooking(item, bookingBefore, bookingAfter);
        }
        ItemDto itemDto = converter.convert(item);
        return itemDto;
    }

    public Collection<ItemDto> getAllItemsByUserId(Long ownerId, Integer from, Integer size) {

        User user = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException("пользователь не найден"));
        Pageable pageable = PageRequest.of(from, size);
        Page<Item> items = itemRepository.findItemByOwnerId(ownerId, pageable);

        return items.stream().map(item -> converter.convertBooking(item,
                        bookingRepository.findFirstBookingByItemIdAndEndBeforeOrderByStartAsc(item.getId(), LocalDateTime.now()),
                        bookingRepository.findFirstBookingByItemIdAndStartAfterOrderByStartAsc(item.getId(), LocalDateTime.now())))
                .sorted(Comparator.comparingLong(ItemDto::getId))
                .collect(Collectors.toList());

    }

    public Collection<ItemDto> searchItemByName(String itemName, Integer from, Integer size) {

        String searchItem = itemName.toLowerCase();
        Pageable pageable = PageRequest.of(from, size);

        Page<Item> items = itemRepository.findItemByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableIsTrue(searchItem, searchItem, pageable);

        return items.stream().map(converter::convert).collect(Collectors.toList());

    }

    public CommentDto addComment(long userId, long itemId, @NotNull @NotBlank CommentRequest comment) {

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

        Comment newComment = new Comment();
        newComment.setText(comment.getText());
        newComment.setCreated(LocalDateTime.now());
        newComment.setItem(item);
        newComment.setUser(user);

        Comment saveComment = commentRepository.save(newComment);

        return commentConverter.convert(commentRepository.getReferenceById(saveComment.getId()));
    }

}
