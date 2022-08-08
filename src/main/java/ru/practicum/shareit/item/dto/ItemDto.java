package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingForItem;

import java.util.List;


@Data
@Builder
public class ItemDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingForItem lastBooking;
    private BookingForItem nextBooking;
    private List<CommentDto> comments;

}
