package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingForItem;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
public class ItemDto {

    public ItemDto() {
    }

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long requestId;
    private BookingForItem lastBooking;
    private BookingForItem nextBooking;
    private List<CommentDto> comments;

}
