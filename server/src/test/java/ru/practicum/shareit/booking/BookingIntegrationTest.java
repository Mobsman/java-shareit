package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingIntegrationTest {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void getAllBookingsOfUser() {
        User user = new User();
        user.setName("test1");
        user.setEmail("user@email.com");
        userRepository.save(user);

        User booker = new User();
        booker.setName("test2");
        booker.setEmail("booker@email.com");
        userRepository.save(booker);

        Item item = new Item();
        item.setOwner(user);
        item.setName("тележка");
        item.setDescription("маленькая тележка");
        item.setAvailable(true);
        itemRepository.save(item);

        Booking lastBooking = new Booking();
        lastBooking.setItem(item);
        lastBooking.setStatus(Status.APPROVED);
        lastBooking.setBooker(booker);
        lastBooking.setStart(LocalDateTime.now().minus(2, ChronoUnit.DAYS));
        lastBooking.setEnd(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        bookingRepository.save(lastBooking);

        Booking nextBooking = new Booking();
        nextBooking.setItem(item);
        nextBooking.setStatus(Status.APPROVED);
        nextBooking.setBooker(booker);
        nextBooking.setStart(LocalDateTime.now().plus(2, ChronoUnit.DAYS));
        nextBooking.setEnd(LocalDateTime.now().plus(3, ChronoUnit.DAYS));
        bookingRepository.save(nextBooking);

        List<BookingDto> past = bookingService.getAllBookingOfCurrentUser(booker.getId(), Status.PAST, 0, 2);
        Assertions.assertFalse(past.isEmpty());
        Assertions.assertEquals(lastBooking.getId(), past.get(0).getId());

        List<BookingDto> future = bookingService.getAllBookingOfCurrentUser(booker.getId(), Status.FUTURE, 0, 2);
        Assertions.assertFalse(future.isEmpty());
        Assertions.assertEquals(nextBooking.getId(), future.get(0).getId());
    }

    @Test
    void getAllItemBookingsOfUser() {
        User user = new User();
        user.setName("test1");
        user.setEmail("user@email.com");
        userRepository.save(user);

        User booker = new User();
        booker.setName("test2");
        booker.setEmail("booker@email.com");
        userRepository.save(booker);

        Item item = new Item();
        item.setOwner(user);
        item.setName("тележка");
        item.setDescription("маленькая тележка");
        item.setAvailable(true);
        itemRepository.save(item);

        Booking lastBooking = new Booking();
        lastBooking.setItem(item);
        lastBooking.setStatus(Status.APPROVED);
        lastBooking.setBooker(booker);
        lastBooking.setStart(LocalDateTime.now().minus(2, ChronoUnit.DAYS));
        lastBooking.setEnd(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        bookingRepository.saveAndFlush(lastBooking);

        Booking nextBooking = new Booking();
        nextBooking.setItem(item);
        nextBooking.setStatus(Status.APPROVED);
        nextBooking.setBooker(booker);
        nextBooking.setStart(LocalDateTime.now().plus(2, ChronoUnit.DAYS));
        nextBooking.setEnd(LocalDateTime.now().plus(3, ChronoUnit.DAYS));
        bookingRepository.saveAndFlush(nextBooking);

        List<BookingDto> past = bookingService.getAllItemBookingsOfCurrentUser(user.getId(), Status.PAST, 0, 2);
        Assertions.assertFalse(past.isEmpty());
        Assertions.assertEquals(lastBooking.getId(), past.get(0).getId());

        List<BookingDto> future = bookingService.getAllItemBookingsOfCurrentUser(user.getId(), Status.FUTURE, 0, 2);
        Assertions.assertFalse(future.isEmpty());
        Assertions.assertEquals(nextBooking.getId(), future.get(0).getId());
    }
}