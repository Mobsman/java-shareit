package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;

    LocalDateTime currentDateTime = LocalDateTime.now();
    User bookerTest1;
    User bookerTest2;
    User bookerTest3;
    User userOwner;
    Item itemTest;

    Booking futureBooking;
    Booking pastBooking;
    Booking currentBooking;

    @BeforeEach
    public void beforeEach() {
        bookerTest1 = userRepository.save(new User(null, "test1", "test@test1.com"));
        bookerTest2 = userRepository.save(new User(null, "test2", "test@test2.com"));
        bookerTest3 = userRepository.save(new User(null, "test3", "test@test3.com"));

        userOwner = userRepository.save(new User(null, "test4", "test@test4.com"));
        itemTest = itemRepository.save(new Item(
                1L,
                "Тележка",
                "маленька тележка",
                true,
                null,
                userOwner,
                null,
                Collections.emptyList()));


        futureBooking = new Booking(
                1L,
                currentDateTime.plusDays(1),
                currentDateTime.plusDays(2),
                itemTest,
                bookerTest1,
                Status.WAITING
        );
        pastBooking = new Booking(
                2L,
                currentDateTime.minusDays(3),
                currentDateTime.minusDays(2),
                itemTest,
                bookerTest2,
                Status.APPROVED
        );
        currentBooking = new Booking(
                3L,
                currentDateTime.minusDays(2),
                currentDateTime.plusDays(2),
                itemTest,
                bookerTest3,
                Status.APPROVED
        );
        bookingRepository.save(futureBooking);
        bookingRepository.save(pastBooking);
        bookingRepository.save(currentBooking);
    }

    @Test
    void testFindBookingsByBookerIdAndEndBeforeOrderByStartDesc() {
        Booking testBooking = bookingRepository.findBookingsByBookerIdAndEndBeforeOrderByStartDesc(pastBooking.getBooker().getId(),
                currentDateTime).get(0);
        assertThat(testBooking.getBooker()).isEqualTo(pastBooking.getBooker());
        assertThat(testBooking.getItem()).isEqualTo(pastBooking.getItem());
    }


    @Test
    void findBookingsByBookerIdAndStartAfterOrderByStartDesc() {
        Booking testBooking = bookingRepository.findBookingsByBookerIdAndStartAfterOrderByStartDesc(futureBooking.getBooker().getId(),
                currentDateTime).get(0);
        assertThat(testBooking.getBooker()).isEqualTo(futureBooking.getBooker());
        assertThat(testBooking.getItem()).isEqualTo(futureBooking.getItem());
    }

    @Test
    void testFindByBookerIdAndStartIsBeforeAndEndIsAfter() {
        Booking testBooking = bookingRepository.findBookingsByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                currentBooking.getBooker().getId(), currentDateTime, currentDateTime).get(0);
        assertThat(testBooking.getBooker()).isEqualTo(currentBooking.getBooker());
        assertThat(testBooking.getItem()).isEqualTo(currentBooking.getItem());
    }

    @Test
    void testFindByBookerIdAndStatus() {
        Booking testBooking = bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(futureBooking.getBooker().getId(),
                Status.WAITING).get(0);
        assertThat(testBooking.getBooker()).isEqualTo(futureBooking.getBooker());
        assertThat(testBooking.getItem()).isEqualTo(futureBooking.getItem());
    }
}
