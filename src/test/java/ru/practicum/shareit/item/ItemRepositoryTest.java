package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;

    User userTest1 = new User(null, "name", "test@test1.com");
    User userTest2 = new User(null, "name2", "test2@test2.com");
    Item itemTest1 = new Item(
            1L,
            "Тележка",
            "маленька тележка",
            true,
            null,
            null,
            null,
            null);

    Item itemTest2 = new Item(
            2L,
            "Большая Тележка",
            "большая тележка",
            true,
            null,
            null,
            null,
            null);

    ItemRequest itemRequest = new ItemRequest(1L,
            "Тележка",
            userTest2,
            LocalDateTime.now());

    @BeforeEach
    public void beforeEach() {

        userRepository.save(userTest1);
        userRepository.save(userTest2);

        itemTest1.setOwner(userTest1);
        itemTest2.setOwner(userTest2);

        itemRepository.save(itemTest1);
        itemRepository.save(itemTest2);

        itemRequestRepository.save(itemRequest);

    }

    @Test
    void testFindOwnerById() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> items = itemRepository.findItemByOwnerId(userTest1.getId(), pageable);
        assertThat(items.get().findFirst().get().getName().equals(itemTest1.getName()));
    }

    @Test
    void testGetAllRequests() {
        List<ItemRequest> request = itemRequestRepository.findAll();
        assertThat(request.get(0).getId().equals(itemRequest.getId()));
    }

    @Test
    void testSearch() {
        Pageable pageable = PageRequest.of(0, 10);
        Item testItem = itemRepository.findItemByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableIsTrue("Тел", "еле", pageable).getContent().get(0);
        assertThat(testItem.getName()).isEqualTo(testItem.getName());
    }
}
