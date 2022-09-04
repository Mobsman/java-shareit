package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserRequest.UserCreateRequest;
import ru.practicum.shareit.user.UserRequest.UserUpdateRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public Object createUser(@RequestBody @Valid UserCreateRequest request) {

        log.info("Creating user {}", request.toString());
        return userClient.createUser(request);
    }

    @PatchMapping("/{userId}")
    public Object updateUser(@PathVariable("userId") @Positive Long userId,
                             @RequestBody @Valid UserUpdateRequest request) {

        log.info("Update user id {}", userId);
        return userClient.updateUser(userId, request);
    }

    @GetMapping("/{userId}")
    public Object getById(@PathVariable("userId") @Positive Long userId) {
        log.info("Get user id {}", userId);
        return userClient.getById(userId);
    }

    @GetMapping
    public Object getAll() {
        log.info("Get all users");
        return userClient.getAll();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") @Positive Long userId) {
        log.info("Delete user id {}", userId);
        userClient.deleteUser(userId);
    }

}