package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {


    List<Booking> findBookingsByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findBookingsByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime time);

    List<Booking> findBookingsByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime time);

    List<Booking> findBookingsByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long bookerId, LocalDateTime time1, LocalDateTime time2);

    List<Booking> findBookingsByBookerIdAndStatusOrderByStartDesc(long bookerId, Status status);


    List<Booking> findBookingsByItemIdInOrderByStartDesc(List<Long> itemId);

    List<Booking> findBookingsByItemIdInAndEndBeforeOrderByStartDesc(List<Long> itemIds, LocalDateTime time);

    List<Booking> findBookingsByItemIdInAndStartBeforeAndEndAfterOrderByStartDesc(List<Long> itemIds, LocalDateTime time1, LocalDateTime time2);

    List<Booking> findBookingsByItemIdInAndStartAfterOrderByStartDesc(List<Long> itemIds, LocalDateTime time);

    List<Booking> findBookingsByItemIdInAndStatusOrderByStartDesc(List<Long> itemIds, Status status);

    Booking findFirstBookingByItemIdAndEndBeforeOrderByStartAsc(long itemId, LocalDateTime time);

    Booking findFirstBookingByItemIdAndStartAfterOrderByStartAsc(long itemId, LocalDateTime time);

    List<Booking> findBookingsByItemIdAndBookerIdAndStatusAndStartBefore(long itemId,
                                                                         long bookerId,
                                                                         Status status,
                                                                         LocalDateTime time);


}
