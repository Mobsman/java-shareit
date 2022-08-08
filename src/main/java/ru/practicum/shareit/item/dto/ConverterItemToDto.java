package ru.practicum.shareit.item.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingForItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

@Component
public class ConverterItemToDto implements Converter<Item, ItemDto> {

    @Override
    public ItemDto convert(Item source) {

        return ItemDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .available(source.getAvailable())
                .comments(source.getComments()).build();


    }

    public ItemDto convertBooking(Item source, Booking bookingBefore, Booking bookingAfter) {


        return ItemDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .available(source.getAvailable())
                .comments(source.getComments())
                .nextBooking(bookingAfter != null ? BookingForItem.builder().id(bookingAfter.getId()).bookerId(bookingAfter.getBooker().getId()).build() : null)
                .lastBooking(bookingBefore != null ? BookingForItem.builder().id(bookingBefore.getId()).bookerId(bookingBefore.getBooker().getId()).build() : null)
                .build();

    }


}

