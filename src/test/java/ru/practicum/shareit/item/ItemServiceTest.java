package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.model.CommentRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;

import static org.mockito.Mockito.when;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    private final ItemService itemService;

    @MockBean
    UserRepository userRepository;
    @MockBean
    ItemRepository itemRepository;
    @MockBean
    BookingRepository bookingRepository;

    User userTest = new User(1L, "name", "test@test1.com");

    Item itemTest = new Item(
            1L,
            "Тележка",
            "маленька тележка",
            true,
            null,
            null,
            null,
            Collections.emptyList());

    CommentRequest comment = new CommentRequest(
            "text"
    );

    @Test
    void testCreateItemWithUserIdIsNull() {
        UserNotFoundException ex = Assertions.assertThrows(
                UserNotFoundException.class,
                () ->
                        itemService.create(null, itemTest));
        Assertions.assertEquals("пользователь не найден",
                ex.getMessage());
    }


    @Test
    void testUpdateItemWithUserNotOwner() {
        when(userRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(userTest);
        when(itemRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(itemTest);
        UserNotFoundException ex = Assertions.assertThrows(
                UserNotFoundException.class,
                () ->
                        itemService.update(1L, itemTest, 1L));
        Assertions.assertEquals("пользователь не найден",
                ex.getMessage());
    }

    @Test
    void testCreateCommentWithUserNotBooker() {
        when(userRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(userTest);
        when(itemRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(itemTest);
        when(bookingRepository.findBookingsByItemIdAndBookerIdAndStatusAndStartBefore(
                Mockito.anyLong(), Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(Collections.emptyList());
        UserNotFoundException ex = Assertions.assertThrows(
                UserNotFoundException.class,
                () ->
                        itemService.addComment(1L, 1L, comment));
        Assertions.assertEquals("пользователь не найден",
                ex.getMessage());
    }
}