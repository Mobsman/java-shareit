package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class CommentRequest {

    public CommentRequest() {
    }

    private String text;

}
