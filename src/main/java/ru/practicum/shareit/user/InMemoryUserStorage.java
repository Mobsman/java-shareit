package ru.practicum.shareit.user;


import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserDao {

    private final Map<Long, User> users = new HashMap<Long, User>();
    private Long id = 0L;

    @Override
    public User create(User user) {

        user.setId(getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user, Long id) {

        User u = users.get(id);

        if (u != null) {
            User update = new User();
            update.setId(id);
            update.setName(user.getName() == null ? u.getName() : user.getName());
            update.setEmail(user.getEmail() == null ? u.getEmail() : user.getEmail());
            users.put(u.getId(), update);
            return user;
        }
        return null;
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    @Override
    public User getUserById(long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private Long getId() {
        id++;
        return id;
    }
}
