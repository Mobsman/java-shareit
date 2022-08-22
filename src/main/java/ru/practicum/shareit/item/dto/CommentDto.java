package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {

    public CommentDto() {

    }

    private long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
