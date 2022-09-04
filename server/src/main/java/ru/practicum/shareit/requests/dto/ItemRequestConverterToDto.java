package ru.practicum.shareit.requests.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

@Component
public class ItemRequestConverterToDto implements Converter<ItemRequest,ItemRequestDto> {

    @Override
    public ItemRequestDto convert(ItemRequest source) {
        return  ItemRequestDto.builder()
                .id(source.getId())
                .created(source.getCreated())
                .requester(source.getRequester())
                .description(source.getDescription()).build();
    }

    public ItemRequestDto converter(ItemRequest source, List<ItemDto> list) {
        return  ItemRequestDto.builder()
                .id(source.getId())
                .created(source.getCreated())
                .requester(source.getRequester())
                .description(source.getDescription())
                .items(list).build();

    }

}
