package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceIntegrationTest {

    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    LocalDateTime currentDateTime = LocalDateTime.now();

    User userTest;
    User bookerTest;
    Item itemTest;
    BookingRequest bookingRequest;


    @BeforeEach
    public void beforeEach() {

        bookerTest = userRepository.save(new User(null, "test1", "test@test1.com"));
        userTest = userRepository.save(new User(null, "test2", "test@test2.com"));
        itemTest = itemRepository.save(new Item(
                1L,
                "Тележка",
                "маленька тележка",
                true,
                null,
                userTest,
                null,
                Collections.emptyList()));

        bookingRequest = new BookingRequest(
                itemTest.getId(),
                currentDateTime.plusDays(2),
                currentDateTime.plusDays(3));
    }

    @Test
    void testAddBooking() {
        Long bookingId = bookingService.addBooking(bookerTest.getId(), bookingRequest).getId();
        BookingDto testBookingDto = bookingService.getBookingById(bookingId, bookerTest.getId());
        assertThat(testBookingDto.getBooker().getId()).isEqualTo(bookerTest.getId());
        assertThat(testBookingDto.getItem().getId()).isEqualTo(itemTest.getId());
    }

    @Test
    void approveOrRejectBooking() {
        Long bookingId = bookingService.addBooking(bookerTest.getId(), bookingRequest).getId();
        bookingService.approveOrRejectBooking(bookingId, false, userTest.getId());
        BookingDto testBookingDto = bookingService.getBookingById(bookingId, bookerTest.getId());
        assertThat(testBookingDto.getStatus()).isEqualTo(Status.REJECTED);
    }

    @Test
    void testGetBooking() {
        Long bookingId = bookingService.addBooking(bookerTest.getId(), bookingRequest).getId();
        BookingDto testBookingDto = bookingService.getBookingById(bookingId, bookerTest.getId());
        assertThat(testBookingDto.getBooker().getId()).isEqualTo(bookerTest.getId());
    }

    @Test
    void getAllBookingOfCurrentUser() {
        Long bookingId = bookingService.addBooking(bookerTest.getId(), bookingRequest).getId();
        List<BookingDto> testBookingsDto = bookingService.getAllBookingOfCurrentUser(bookerTest.getId(), Status.valueOf("FUTURE"));
        assertThat(testBookingsDto.get(0).getId()).isEqualTo(bookingId);
    }

    @Test
    void getAllItemBookingsOfCurrentUser() {
        Long bookingId = bookingService.addBooking(bookerTest.getId(), bookingRequest).getId();
        List<BookingDto> testBookingsDto = bookingService.getAllItemBookingsOfCurrentUser(userTest.getId(), Status.valueOf("FUTURE"));
        assertThat(testBookingsDto.get(0).getId()).isEqualTo(bookingId);
    }
}