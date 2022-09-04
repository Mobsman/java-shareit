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
        return userClient.createUser(request);
    }

    @PatchMapping("/{userId}")
    public Object updateUser(@PathVariable("userId") @Positive Long userId,
                             @RequestBody @Valid UserUpdateRequest request) {
        return userClient.updateUser(userId, request);
    }

    @GetMapping("/{userId}")
    public Object getById(@PathVariable("userId") @Positive Long userId) {
        return userClient.getById(userId);
    }

    @GetMapping
    public Object getAll() {
        return userClient.getAll();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") @Positive Long userId) {
        userClient.deleteUser(userId);
    }

}