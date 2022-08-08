package ru.practicum.shareit.item.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingForItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConverterItemToDto implements Converter<Item, ItemDto> {


    @Override
    public ItemDto convert(Item source) {

        return ItemDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .available(source.getAvailable())
                .comments(source.getComments() == null ? null : source.getComments().stream().map(ConverterCommentItemToCommentDto::converter).collect(Collectors.toList())).build();


    }

    public ItemDto convertBooking(Item source, Booking bookingBefore, Booking bookingAfter) {


        return ItemDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .available(source.getAvailable())
                .comments(source.getComments() == null ? null : source.getComments().stream().map(ConverterCommentItemToCommentDto::converter).collect(Collectors.toList()))
                .nextBooking(bookingAfter != null ? BookingForItem.builder().id(bookingAfter.getId()).bookerId(bookingAfter.getBooker().getId()).build() : null)
                .lastBooking(bookingBefore != null ? BookingForItem.builder().id(bookingBefore.getId()).bookerId(bookingBefore.getBooker().getId()).build() : null)
                .build();

    }


}

