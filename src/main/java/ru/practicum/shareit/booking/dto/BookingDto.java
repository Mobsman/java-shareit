package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {

    private long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime start;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime end;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Status status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User booker;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Item item;

}
