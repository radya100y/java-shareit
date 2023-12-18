package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public UserDto save(UserDto user) {
        return UserMapper.toUserDto(userStorage.save(UserMapper.toUser(user)));
    }

    public UserDto get(Long id) {
        return UserMapper.toUserDto(userStorage.get(id));
    }

    public UserDto update(UserDto user) {
        return UserMapper.toUserDto(userStorage.update(UserMapper.toUser(user)));
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
