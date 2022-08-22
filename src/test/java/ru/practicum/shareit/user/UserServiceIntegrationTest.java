package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceIntegrationTest {

    private final UserService userService;

    User user1 = new User(null, "test1", "test@test1.com");
    User user2 = new User(null, "test2", "test@test2.com");

    @Test
    void testCreateUser() {
        Long userId = userService.create(user1).getId();
        UserDto testUserDto = userService.getUserById(userId);
        assertThat(testUserDto.getId()).isEqualTo(userId);
        assertThat(testUserDto.getName()).isEqualTo(user1.getName());
    }

    @Test
    void testGetUserById() {
        Long userId = userService.create(user1).getId();
        userService.create(user1);
        UserDto testUserDto = userService.getUserById(userId);
        assertThat(testUserDto.getId()).isEqualTo(userId);
        assertThat(testUserDto.getName()).isEqualTo(user1.getName());
    }

    @Test
    void testDeleteUser() {
        Long userId = userService.create(user1).getId();
        Collection<UserDto> testUsersDto = userService.getAllUsers();
        assertThat(testUsersDto).hasSize(1);
        userService.delete(userId);
        testUsersDto = userService.getAllUsers();
        assertThat(testUsersDto).hasSize(0);
    }

    @Test
    void testUpdateUser() {
        Long userId = userService.create(user1).getId();
        user1.setName("имя");
        userService.update(user1, userId);
        UserDto testUserDto = userService.getUserById(userId);
        assertThat(testUserDto.getId()).isEqualTo(userId);
        assertThat(testUserDto.getName()).isEqualTo("имя");
    }

    @Test
    void testGetAllUsers() {
        userService.create(user1);
        userService.create(user2);
        Collection<UserDto> testUsersDto = userService.getAllUsers();
        assertThat(testUsersDto).hasSize(2);
    }


}