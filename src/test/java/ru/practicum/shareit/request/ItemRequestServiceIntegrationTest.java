package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceIntegrationTest {


    private final UserRepository userRepository;
    private final ItemRequestService itemRequestService;
    private final ItemRepository itemRepository;

    User user = new User(null, "test", "test@test1.com");
    User user2 = new User(null, "test", "test@test2.com");
    ItemRequest itemRequest1 = new ItemRequest(
            null,
            "запрос1",
            null,
            LocalDateTime.now()
    );
    ItemRequest itemRequest2 = new ItemRequest(
            null,
            "запрос2",
            null,
            LocalDateTime.now()
    );


    @Test
    void testCreateItemRequest() {
        User userTest = userRepository.save(user);
        Long itemRequestId = itemRequestService.createRequest(itemRequest1, userTest.getId()).getId();
        ItemRequestDto testItemRequestDto = itemRequestService.geyRequestById(itemRequestId, user.getId());
        assertThat(testItemRequestDto.getDescription()).isEqualTo(itemRequest1.getDescription());
        assertThat(testItemRequestDto.getRequester().getId()).isEqualTo(user.getId());
    }

    @Test
    void testGetItemRequestsForUser() {
        User userTest = userRepository.save(user);
        User userTest2 = userRepository.save(user2);
        ItemRequestDto itemRequestTest1 = itemRequestService.createRequest(itemRequest1, userTest.getId());
        ItemRequestDto itemRequestTest2 = itemRequestService.createRequest(itemRequest2, userTest.getId());
        Collection<ItemRequestDto> testItemRequestsDto = itemRequestService.getAllRequest(userTest2.getId(), 0, 10);
        assertThat(testItemRequestsDto).hasSize(2);
    }


    @Test
    void testGetItemRequestById() {
        User userTest = userRepository.save(user);
        ItemRequestDto itemRequestTest1 = itemRequestService.createRequest(itemRequest1, userTest.getId());
        ItemRequestDto testItemRequestDto = itemRequestService.geyRequestById(itemRequestTest1.getId(), userTest.getId());
        assertThat(testItemRequestDto.getDescription()).isEqualTo(itemRequestTest1.getDescription());
        assertThat(testItemRequestDto.getRequester().getId()).isEqualTo(userTest.getId());
    }
}
