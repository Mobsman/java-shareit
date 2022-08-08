package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.ConverterBookingToDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BookingService {


    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ConverterBookingToDto convert;

    private final LocalDateTime currentTime = LocalDateTime.now();

    public BookingDto addBooking(long bookerId, BookingRequest booking) {

        Optional<User> user = userRepository.findById(bookerId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (booking.getStart().isAfter(booking.getEnd()) ||
                booking.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingTimeException("Неправильно указано время");
        }

        Optional<Item> itemRepo = itemRepository.findById(booking.getItemId());

        if (itemRepo.isEmpty()) {
            throw new ItemNotFoundException("Предмет не найден");
        }

        if (itemRepo.get().getOwner().getId().equals(user.get().getId())) {
            throw new BookingBookerException("Этот предмет ваш");
        }

        if (itemRepo.get().getAvailable().equals(false)) {
            throw new BookingStatusException("Предмет недоступен для аренды");
        }

        Booking newBooking = new Booking();
        newBooking.setBooker(user.get());
        newBooking.setItem(itemRepo.get());
        newBooking.setStart(booking.getStart());
        newBooking.setEnd(booking.getEnd());
        newBooking.setStatus(Status.WAITING);
        return convert.convert(bookingRepository.save(newBooking));
    }

    public BookingDto getBookingById(long bookingId, long userId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        Optional<Booking> booking = bookingRepository.findById(bookingId);

        if (!user.get().getId().equals(booking.get().getBooker().getId()) &&
                !booking.get().getItem().getOwner().getId().equals(userId)) {
            throw new BookingNotFoundException("Заявка не найдена");
        }
        return convert.convert(booking.get());
    }

    public BookingDto approveOrRejectBooking(long bookingId, Boolean isApproved, long ownerId) {

        Optional<Booking> booking = bookingRepository.findById(bookingId);
        Optional<User> owner = userRepository.findById(ownerId);

        if (owner.get().getId().equals(booking.get().getBooker().getId()) || !owner.get().getId().equals(booking.get().getItem().getOwner().getId())) {
            throw new ItemNotFoundException("Это не ваш предмет.");
        }

        if (booking.get().getStatus() != Status.WAITING) {
            throw new BookingStatusException("Статус аренды уже изменен.");
        }

        booking.get().setStatus(isApproved ? Status.APPROVED : Status.REJECTED);

        return convert.convert(bookingRepository.save(booking.get()));


    }

    public List<BookingDto> getAllBookingOfCurrentUser(long bookerId, Status status) {

        switch (status) {
            case ALL:
                return checkEmptyList(bookingRepository.findBookingsByBookerIdOrderByStartDesc(bookerId)
                        .stream().map(convert::convert).collect(Collectors.toList()));
            case PAST:
                return bookingRepository.findBookingsByBookerIdAndEndBeforeOrderByStartDesc(bookerId, currentTime)
                        .stream().map(convert::convert).collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingsByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId, currentTime, currentTime)
                        .stream().map(convert::convert).collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByBookerIdAndStartAfterOrderByStartDesc(
                        bookerId, currentTime).stream().map(convert::convert).collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(
                        bookerId, Status.REJECTED).stream().map(convert::convert).collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(
                        bookerId, Status.WAITING).stream().map(convert::convert).collect(Collectors.toList());
            default:
                throw new BookingStatusException("ошибка");
        }
    }

    public List<BookingDto> getAllItemBookingsOfCurrentUser(long ownerId, Status status) {

        Optional<User> owner = userRepository.findById(ownerId);

        List<Item> items = itemRepository.findItemByOwnerId(ownerId);

        List<Long> itemsId = items.stream().map(Item::getId).collect(Collectors.toList());

        if (items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ошибка");
        }

        switch (status) {
            case ALL:
                return checkEmptyList(bookingRepository.findBookingsByItemIdInOrderByStartDesc(itemsId)
                        .stream().map(convert::convert).collect(Collectors.toList()));
            case PAST:
                return bookingRepository.findBookingsByItemIdInAndEndBeforeOrderByStartDesc(itemsId, currentTime)
                        .stream().map(convert::convert).collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingsByItemIdInAndStartBeforeAndEndAfterOrderByStartDesc(itemsId, currentTime, currentTime)
                        .stream().map(convert::convert).collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByItemIdInAndStartAfterOrderByStartDesc(
                        itemsId, currentTime).stream().map(convert::convert).collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingsByItemIdInAndStatusOrderByStartDesc(
                        itemsId, Status.REJECTED).stream().map(convert::convert).collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingsByItemIdInAndStatusOrderByStartDesc(
                        itemsId, Status.WAITING).stream().map(convert::convert).collect(Collectors.toList());
            default:
                throw new BookingStatusException("ошибка");
        }

    }

    private List<BookingDto> checkEmptyList(List list) {
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ошибка");
        } else return list;
    }
}
