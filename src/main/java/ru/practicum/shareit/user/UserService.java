package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User save(User user) {
        return userStorage.save(user);
    }

    public User get(Long id) {
        return userStorage.get(id);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public void delete(long id) {
        userStorage.delete(id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }
}
