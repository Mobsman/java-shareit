package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.ConverterBookingToDto;
import ru.practicum.shareit.booking.exception.BookingStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ItemRepository mockItemRepository;
    @Mock
    private BookingRepository mockBookingRepository;
    @Mock
    private ConverterBookingToDto converter;


    @Test
    void addBooking() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("description");
        User owner = new User();
        owner.setId(1L);
        item.setOwner(owner);
        item.setAvailable(true);

        User booker = new User();
        booker.setId(2L);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBooker(booker);
        bookingDto.setId(1L);
        bookingDto.setItem(item);
        bookingDto.setStatus(Status.WAITING);

        Mockito.when(mockUserRepository.findById(Mockito.eq(2L))).thenReturn(Optional.of(booker));
        Mockito.when(mockItemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Mockito.when(converter.convert(Mockito.any())).thenReturn(bookingDto);

        Mockito.when(mockBookingRepository.save(Mockito.any(Booking.class)))
                .thenAnswer((Answer<Booking>) invocationOnMock -> {
                    Booking savedBooking = invocationOnMock.getArgument(0);
                    savedBooking.setId(1);
                    return savedBooking;
                });


        BookingRequest booking = new BookingRequest();
        booking.setStart(LocalDateTime.now().plusSeconds(10));
        booking.setEnd(LocalDateTime.now().plusSeconds(20));
        booking.setItemId(1L);
        BookingDto result = bookingService.addBooking(2L, booking);
        assertEquals(1L, result.getId());
        assertEquals(item.getId(), result.getItem().getId());
        assertEquals(Status.WAITING, result.getStatus());
        assertEquals(booker.getId(), result.getBooker().getId());
    }

    @Test
    void addBookingRequestBadUser() {
        BookingRequest booking = new BookingRequest();

        assertThrows(UserNotFoundException.class,
                () -> bookingService.addBooking(-2L, booking));
    }

    @Test
    void addBookingRequestItemNotAvailable() {
        Item item = new Item();
        item.setId(1L);
        item.setName("тележка");
        item.setDescription("маленькая тележка");
        User owner = new User();
        owner.setId(1L);
        item.setOwner(owner);
        item.setAvailable(false);

        User booker = new User();
        booker.setId(2L);

        BookingRequest booking = new BookingRequest();
        booking.setStart(LocalDateTime.now().plusSeconds(10));
        booking.setEnd(LocalDateTime.now().plusSeconds(30));
        booking.setItemId(1L);
        Mockito.when(mockUserRepository.findById(Mockito.eq(2L))).thenReturn(Optional.of(booker));

        assertThrows(ItemNotFoundException.class,
                () -> bookingService.addBooking(2L, booking));
    }

    @Test
    void approveOrRejectBookingApproved() {
        User owner = new User();
        owner.setId(1L);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(Status.WAITING);
        User booker = new User();
        booker.setId(2L);
        booking.setBooker(booker);
        booking.setItem(item);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStatus(Status.APPROVED);

        Mockito.when(mockUserRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(owner));
        Mockito.when(mockBookingRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(booking));
        Mockito.when(converter.convert(Mockito.any())).thenReturn(bookingDto);
        BookingDto result = bookingService.approveOrRejectBooking(booking.getId(), true, owner.getId());
        assertEquals(Status.APPROVED, result.getStatus());
        Mockito.verify(mockBookingRepository, Mockito.times(1)).save(Mockito.any(Booking.class));
    }

    @Test
    void approveOrRejectBookingRejected() {
        User owner = new User();
        owner.setId(1L);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(Status.WAITING);
        User booker = new User();
        booker.setId(2L);
        booking.setBooker(booker);
        booking.setItem(item);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStatus(Status.REJECTED);

        Mockito.when(mockUserRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(owner));
        Mockito.when(mockBookingRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(booking));
        Mockito.when(converter.convert(Mockito.any())).thenReturn(bookingDto);
        BookingDto result = bookingService.approveOrRejectBooking(booking.getId(), false, owner.getId());
        assertEquals(Status.REJECTED, result.getStatus());
        Mockito.verify(mockBookingRepository, Mockito.times(1)).save(Mockito.any(Booking.class));
    }

    //
    @Test
    void approveOrRejectBookingNotWaiting() {
        User owner = new User();
        owner.setId(1L);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        User booker = new User();
        booker.setId(2L);
        booking.setBooker(booker);
        booking.setItem(item);

        Mockito.when(mockUserRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(owner));
        Mockito.when(mockBookingRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(booking));

        assertThrows(BookingStatusException.class,
                () -> bookingService.approveOrRejectBooking(booking.getId(), true, owner.getId()));
    }

    @Test
    void approveOrRejectBookingRequesterIsBooker() {
        User owner = new User();
        owner.setId(1L);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        User booker = new User();
        booker.setId(2L);
        booking.setBooker(booker);
        booking.setItem(item);

        Mockito.when(mockUserRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(owner));
        Mockito.when(mockBookingRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(booking));

        assertThrows(NoSuchElementException.class,
                () -> bookingService.approveOrRejectBooking(booking.getId(), true, booker.getId()));
    }


    @Test
    void getBookingByIdRequesterBooker() {
        User owner = new User();
        owner.setId(1L);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(Status.WAITING);
        User booker = new User();
        booker.setId(2L);
        booking.setBooker(booker);
        booking.setItem(item);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);

        Mockito.when(mockUserRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(owner));
        Mockito.when(mockUserRepository.findById(Mockito.eq(2L))).thenReturn(Optional.of(booker));
        Mockito.when(mockBookingRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(booking));
        Mockito.when(converter.convert(Mockito.any())).thenReturn(bookingDto);

        BookingDto result = bookingService.getBookingById(booking.getId(), booker.getId());
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void getBookingByIdRequesterOwner() {
        User owner = new User();
        owner.setId(1L);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(Status.WAITING);
        User booker = new User();
        booker.setId(2L);
        booking.setBooker(booker);
        booking.setItem(item);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);

        Mockito.when(mockUserRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(owner));
        Mockito.when(mockUserRepository.findById(Mockito.eq(2L))).thenReturn(Optional.of(booker));
        Mockito.when(mockBookingRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(booking));
        Mockito.when(converter.convert(Mockito.any())).thenReturn(bookingDto);
        BookingDto result = bookingService.getBookingById(booking.getId(), owner.getId());
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void getBookingByIdRequesterUnknown() {
        User owner = new User();
        owner.setId(1L);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(Status.WAITING);
        User booker = new User();
        booker.setId(2L);
        booking.setBooker(booker);
        booking.setItem(item);

        Mockito.when(mockUserRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(owner));
        Mockito.when(mockUserRepository.findById(Mockito.eq(2L))).thenReturn(Optional.of(booker));
        Mockito.when(mockBookingRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(booking));

        assertThrows(UserNotFoundException.class,
                () -> bookingService.getBookingById(booking.getId(), 3L));
    }
}
