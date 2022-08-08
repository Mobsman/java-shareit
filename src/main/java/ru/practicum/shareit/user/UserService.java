package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.ConverterUserToUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;
    private ConverterUserToUserDto converter;

    UserDto create(User user) {

        repository.save(user);
        return converter.convert(repository.getReferenceById(user.getId()));
    }

    UserDto update(User user, Long id) {

        User u = repository.getReferenceById(id);

        if (u != null) {
            User update = new User();
            update.setId(id);
            update.setName(user.getName() == null ? u.getName() : user.getName());
            update.setEmail(user.getEmail() == null ? u.getEmail() : user.getEmail());
            repository.save(update);
            return converter.convert(repository.getReferenceById(id));
        }
        return null;
    }

    void delete(long id) {

        repository.delete(repository.getReferenceById(id));
    }

    UserDto getUserById(long id) {

        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            return converter.convert(repository.getReferenceById(id));
        }
        throw new UserNotFoundException("пользователь не найден");
    }

    List<UserDto> getAllUsers() {

        List<User> users = repository.findAll();
        return users.stream().map(converter::convert).collect(Collectors.toList());
    }

}
