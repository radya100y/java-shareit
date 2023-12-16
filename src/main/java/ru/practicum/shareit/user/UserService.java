package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto save(User user) {
        return UserMapper.toUserDto(userStorage.save(user));
    }

    public UserDto get(Long id) {
        return UserMapper.toUserDto(userStorage.get(id));
    }

    public UserDto update(User user) {
        return UserMapper.toUserDto(userStorage.update(user));
    }

    public void delete(long id) {
        userStorage.delete(id);
    }

    public List<UserDto> getAll() {
        return userStorage.getAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
