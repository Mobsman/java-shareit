package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;

    User userTest1;
    User userTest2;

    ItemRequest itemRequest = new ItemRequest(
            null,
            "маленька тележк",
            null,
            LocalDateTime.now()
    );


    Item itemTest1 = new Item(
            null,
            "Тележка",
            "маленька тележка",
            true,
            null,
            userTest1,
            null,
            Collections.emptyList());

    @BeforeEach
    public void beforeEach() {
        userTest1 = userRepository.save(new User(null, "name", "test@test1.com"));
        userTest2 = userRepository.save(new User(null, "name", "test@test2.com"));
        itemRequest.setRequester(userTest1);
        itemTest1.setRequest(itemRequestRepository.save(itemRequest));
    }

    @Test
    void testFindByRequesterId() {
        ItemRequest testItemRequest = itemRequestRepository.findById(userTest1.getId()).get();
        assertThat(testItemRequest.getDescription()).isEqualTo(itemRequest.getDescription());
    }

    @Test
    void testGetAllByRequester_IdNot() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        ItemRequest testItemRequest = itemRequestRepository.getAllByRequester_IdNot(userTest2.getId(), pageable)
                .getContent().get(0);
        assertThat(testItemRequest.getDescription()).isEqualTo(itemRequest.getDescription());
    }
}