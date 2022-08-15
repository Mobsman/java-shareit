package ru.practicum.shareit.user.dto;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;


@Component
public class ConverterUserToUserDto implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User source) {

        return UserDto.builder()
                .id(source.getId())
                .name(source.getName())
                .email(source.getEmail()).build();


    }
}
