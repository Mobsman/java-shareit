package ru.practicum.shareit.item.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;

@Component
public class ConverterCommentItemToCommentDto implements Converter<Comment, CommentDto> {


    @Override
    public CommentDto convert(Comment source) {

       return CommentDto.builder()
                .id(source.getId())
                .text(source.getText())
                .authorName(source.getUser().getName())
                .created(source.getCreated()).build();
    }
}
