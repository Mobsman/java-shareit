package ru.practicum.shareit.requests.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemRequestDto {

    public ItemRequestDto() {

    }

    private Long id;
    private String description;
    private User requester;
    private LocalDateTime created;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ItemDto> items;

}
