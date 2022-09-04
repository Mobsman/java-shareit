package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {


    Page<Booking> findBookingsByBookerIdOrderByStartDesc(long bookerId, Pageable pageable);

    Page<Booking> findBookingsByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime time, Pageable pageable);

    Page<Booking> findBookingsByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime time, Pageable pageable);

    Page<Booking> findBookingsByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long bookerId, LocalDateTime time1, LocalDateTime time2, Pageable pageable);

    Page<Booking> findBookingsByBookerIdAndStatusOrderByStartDesc(long bookerId, Status status, Pageable pageable);


    Page<Booking> findBookingsByItemIdInOrderByStartDesc(List<Long> itemId, Pageable pageable);

    Page<Booking> findBookingsByItemIdInAndEndBeforeOrderByStartDesc(List<Long> itemIds, LocalDateTime time, Pageable pageable);

    Page<Booking> findBookingsByItemIdInAndStartBeforeAndEndAfterOrderByStartDesc(List<Long> itemIds, LocalDateTime time1, LocalDateTime time2, Pageable pageable);

    Page<Booking> findBookingsByItemIdInAndStartAfterOrderByStartDesc(List<Long> itemIds, LocalDateTime time, Pageable pageable);

    Page<Booking> findBookingsByItemIdInAndStatusOrderByStartDesc(List<Long> itemIds, Status status, Pageable pageable);

    Booking findFirstBookingByItemIdAndEndBeforeOrderByStartAsc(long itemId, LocalDateTime time);

    Booking findFirstBookingByItemIdAndStartAfterOrderByStartAsc(long itemId, LocalDateTime time);

    List<Booking> findBookingsByItemIdAndBookerIdAndStatusAndStartBefore(long itemId,
                                                                         long bookerId,
                                                                         Status status,
                                                                         LocalDateTime time);


}
