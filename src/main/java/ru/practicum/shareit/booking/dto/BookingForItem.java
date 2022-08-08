package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BookingForItem {


    private long id;
    private Long bookerId;

}
