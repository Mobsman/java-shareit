package ru.practicum.shareit.item.itemRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private Long id;
    @NotEmpty
    @NotNull
    private String text;
    private String authorName;
    private LocalDateTime created;
}
