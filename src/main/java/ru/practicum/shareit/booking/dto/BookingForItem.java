package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.User;


@Data
@Builder
public class BookingForItem {


    private long id;
    private Long bookerId;

}
