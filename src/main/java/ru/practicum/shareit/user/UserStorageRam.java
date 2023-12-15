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
        if (!validate(user)) throw new AlreadyExistException("Email " + user.getEmail() + " already exists");
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
        if (!validate(user)) throw new AlreadyExistException("Email " + user.getEmail() + " already exists");
        for (User userFind : users) {
            if (userFind.getId() == user.getId()) {
                if (user.getName() != null) userFind.setName(user.getName());
                if (user.getEmail() != null) userFind.setEmail(user.getEmail());
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

    public boolean validate(User user) { // При размещении данных в БД - вынести проверку уникальности на constraint
        if (user.getEmail() == null) return true;
        for (User userFind : users) {
            if ((user.getEmail().equals(userFind.getEmail())) && (user.getId() != userFind.getId())) return false;
        }
        return true;
    }
}
