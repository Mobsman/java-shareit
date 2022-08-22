package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.CommentRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceIntegrationTest {

    private final ItemService itemService;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    LocalDateTime currentDateTime = LocalDateTime.now();

    User user = new User(null, "name", "test@test.com");
    User user2 = new User(null, "name2", "test@test2.com");

    Item item = new Item(
            1L,
            "Тележка",
            "маленька тележка",
            true,
            null,
            null,
            null,
            null);

    @Test
    void testCreateItem() {
        Long userId = userRepository.save(user).getId();
        Long itemId = itemService.create(userId, item).getId();
        ItemDto testItemDto = itemService.getById(itemId, userId);
        assertThat(testItemDto.getName()).isEqualTo(item.getName());
        assertThat(testItemDto.getDescription()).isEqualTo(item.getDescription());
    }

    @Test
    void testUpdateItem() {
        Long userId = userRepository.save(user).getId();
        Long itemId = itemService.create(userId, item).getId();
        item.setName("Большая тележка");
        item.setAvailable(false);
        itemService.update(userId, item, itemId);
        ItemDto testItemDto = itemService.getById(itemId, userId);
        assertThat(testItemDto.getName()).isEqualTo("Большая тележка");
        assertThat(testItemDto.getAvailable()).isFalse();
    }

    @Test
    void testSearchItem() {
        Long userId = userRepository.save(user).getId();
        itemService.create(userId, item);
        Collection<ItemDto> itemsDto = itemService.searchItemByName("теле", 0, 10);
        assertThat(new ArrayList<>(itemsDto).get(0).getName()).isEqualTo("Тележка");
    }

}

