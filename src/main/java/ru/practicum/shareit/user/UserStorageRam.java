package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.error.AlreadyExistException;
import ru.practicum.shareit.error.NotFoundException;

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

        validateEmail(user);
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

        validateEmail(user);
        User userFind = get(user.getId());
        if (user.getName() != null) userFind.setName(user.getName());
        if (user.getEmail() != null) userFind.setEmail(user.getEmail());
        return userFind;
    }

    @Override
    public void delete(long id) {

        users.remove(get(id));
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    public void validateEmail(User user) {// При размещении данных в БД-вынести проверку уникальности на constraint
        if (user.getEmail() == null) return;
        for (User userFind : users) {
            if ((user.getEmail().equals(userFind.getEmail())) && (user.getId() != userFind.getId()))
                throw new AlreadyExistException("Email " + user.getEmail() + " already exists");
        }
    }
}
