package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ConverterItemToDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.dto.ItemRequestConverterToDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.exception.RequestNotFoundException;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestService {

    UserRepository userRepository;
    ItemRequestRepository itemRequestRepository;
    ItemRepository itemRepository;
    ItemRequestConverterToDto converter;
    ConverterItemToDto itemConverter;

    public ItemRequestDto createRequest(ItemRequest itemRequest, Long requesterId) {

        User user = userRepository.findById(requesterId).orElseThrow(() -> new UserNotFoundException("пользователь не найден"));

        LocalDateTime createTime = LocalDateTime.now();

        ItemRequest request = new ItemRequest();
        request.setRequester(userRepository.findById(requesterId).orElseThrow(() -> new UserNotFoundException("пользователь не найден")));
        request.setDescription(itemRequest.getDescription());
        request.setCreated(createTime);

        return converter.convert(itemRequestRepository.save(request));


    }

    public List<ItemRequestDto> getRequest(Long requesterId) {

        User user = userRepository.findById(requesterId).orElseThrow(() -> new UserNotFoundException("пользователь не найден"));

        List<ItemRequest> itemRequest = itemRequestRepository.findAllByRequester_Id(requesterId);

        return itemRequest.stream().map(i -> converter.converter(i, getItems(requesterId))).collect(Collectors.toList());
    }


    public List<ItemRequestDto> getAllRequest(Long requesterId, Integer from, Integer size) {

        User user = userRepository.findById(requesterId).orElseThrow(() -> new UserNotFoundException("пользователь не найден"));

        Pageable pageable = PageRequest.of(from, size);

        Page<ItemRequest> itemRequestPage = itemRequestRepository.getAllByRequester_IdNot(requesterId, pageable);
        List<Item> items = null;

        try {
            items = itemRepository.findItemByRequest(itemRequestPage.get().findFirst().get().getId());
        } catch (Exception e) {
            return new ArrayList<>();
        }

        List<ItemDto> itemsDto = items.stream().map(i -> itemConverter.convert(i)).collect(Collectors.toList());

        return itemRequestPage.stream().map(i -> converter.converter(i, itemsDto)).collect(Collectors.toList());

    }

    public ItemRequestDto geyRequestById(Long requestId, Long requesterId) {

        User user = userRepository.findById(requesterId).orElseThrow(() -> new UserNotFoundException("пользователь не найден"));

        ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException("запрос не найден"));

        if (!requesterId.equals(request.getRequester().getId())) {
            return converter.converter(request, getItems(request.getRequester().getId()));
        }

        return converter.converter(request, getItems(requesterId));

    }


    private List<ItemDto> getItems(Long requesterId) {

        User user = userRepository.findById(requesterId).orElseThrow(() -> new UserNotFoundException("пользователь не найден"));
        List<Item> items = itemRepository.findItemByRequest(requesterId);
        return items.stream().map(i -> itemConverter.convert(i)).collect(Collectors.toList());

    }
}
