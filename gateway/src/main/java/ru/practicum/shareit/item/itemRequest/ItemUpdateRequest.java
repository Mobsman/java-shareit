package ru.practicum.shareit.item.itemRequest;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemUpdateRequest {

    private String name;
    private String description;
    private Boolean available;
    private Long requestId;

}