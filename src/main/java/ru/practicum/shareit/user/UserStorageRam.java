package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.NotFoundException;

import java.util.List;

@Repository
public class UserStorageRam implements UserStorage {

    private final List<User> users;

    private long id = 0L;

    public UserStorageRam(List<User> userList) {
        this.users = userList;
    }

    @Override
    public User save(User user) {
        user.setId(++id);
        users.add(user);
        return user;
    }

    @Override
    public User get(long id) {
        for (User user : users) {
            if (user.getId() == id) return user;
        }
        throw new NotFoundException("User " + id + " not found");
    }

    @Override
    public User update(User user) {
        for (User userFind : users) {
            if (userFind.getId() == user.getId()) {
                if (!user.getName().isBlank()) userFind.setName(user.getName());
                if (!user.getEmail().isBlank()) userFind.setEmail(user.getEmail());
                return userFind;
            }
        }
        throw new NotFoundException("User " + id + " not found");
    }

    @Override
    public void delete(long id) {
        for (User user : users) {
            if (user.getId() == id) {
                users.remove(user);
                return;
            }
        }
        throw new NotFoundException("User " + id + " not found");
    }

    @Override
    public List<User> getAll() {
        return users;
    }
}
