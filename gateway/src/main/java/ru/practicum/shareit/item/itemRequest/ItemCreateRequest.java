package ru.practicum.shareit.item.itemRequest;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateRequest {

    @NotNull
    @NotEmpty
    private String name;

    @NotEmpty
    @NotNull
    private String description;

    @NotNull
    private Boolean available;

    private Long requestId;

}
