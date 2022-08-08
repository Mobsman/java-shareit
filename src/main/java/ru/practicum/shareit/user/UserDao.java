package ru.practicum.shareit.user;

import java.util.List;

public interface UserDao {


    User create(User user);

    User update(User user, Long id);

    void delete(long id);

    User getUserById(long id);

    List<User> getAllUsers();
}
