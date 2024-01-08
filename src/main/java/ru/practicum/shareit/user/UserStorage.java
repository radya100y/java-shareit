package ru.practicum.shareit.user;

import java.util.List;
@Deprecated
public interface UserStorage {

    User save(User user);

    User get(long id);

    User update(User user);

    void delete(long id);

    List<User> getAll();
}
