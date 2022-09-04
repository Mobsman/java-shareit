package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import javax.validation.Valid;
import java.util.Collection;



@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {


    private final UserService service;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody User user) {
        return service.create(user);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable long id, @RequestBody User user) {
        return service.update(user, id);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        return service.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        service.delete(id);
    }

    @GetMapping()
    public Collection<UserDto> getAllUsers() {
        return service.getAllUsers();
    }

}
