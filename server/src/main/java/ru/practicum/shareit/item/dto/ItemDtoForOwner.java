package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Data
@Builder
public class ItemDtoForOwner {


    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private List<Comment> comments;
    private Booking lastBooking;
    private Booking nextBooking;

}
