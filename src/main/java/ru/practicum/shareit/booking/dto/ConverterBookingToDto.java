package ru.practicum.shareit.booking.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;


@Component
public class ConverterBookingToDto implements Converter<Booking, BookingDto> {

    @Override
    public BookingDto convert(Booking source) {

        return BookingDto.builder()
                .id(source.getId())
                .start(source.getStart())
                .end(source.getEnd())
                .status(source.getStatus())
                .booker(source.getBooker())
                .item(source.getItem())
                .build();

    }


}
