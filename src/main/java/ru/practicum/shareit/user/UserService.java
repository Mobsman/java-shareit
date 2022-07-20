package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.ConverterUserToUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserDao userDao;
    private ConverterUserToUserDto converter;

    UserDto create(User user) {

        checkDuplicateEmail(user);
        userDao.create(user);
        return converter.convert(userDao.getUserById(user.getId()));
    }

    UserDto update(User user, Long id) {

        checkDuplicateEmail(user);
        userDao.update(user, id);
        return converter.convert(userDao.getUserById(id));
    }

    void delete(long id) {

        userDao.delete(id);
    }

    UserDto getUserById(long id) {

        return converter.convert(userDao.getUserById(id));
    }

    List<UserDto> getAllUsers() {

        List<User> users = userDao.getAllUsers();
        return users.stream().map(converter::convert).collect(Collectors.toList());
    }

    private void checkDuplicateEmail(User user) {

        List<User> users = userDao.getAllUsers();

        List<User> email = users.stream().filter(u -> u.getEmail().equals(user.getEmail())).collect(Collectors.toList());

        if (!email.isEmpty()) {
            throw new DuplicateEmailException("Пользователь с таким email уже существует!");
        }

    }

}
