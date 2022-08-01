package ru.practicum.shareit.item.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

@Component
public class ConverterItemToDto  implements Converter<Item, ItemDto> {

    @Override
    public ItemDto convert(Item source) {

        return ItemDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .available(source.getAvailable())
                .owner(source.getOwner())
                .request(source.getRequest()).build();
    }

}

