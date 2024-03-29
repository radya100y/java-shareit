package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.AlreadyExistException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userStorage;

    @Transactional
    public UserDto save(UserDto user) {
        try {
            return UserMapper.toUserDto(userStorage.save(UserMapper.toUser(user)));
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistException("Пользователь с адресом " + user.getEmail() + " уже существует");
        }
    }

    public User getModel(Long id) {
        return userStorage.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + id + " не найден"));
    }

    public UserDto get(Long id) {
        return UserMapper.toUserDto(getModel(id));
    }

    @Transactional
    public UserDto update(UserDto user) {
        UserDto savedUser = UserMapper.toUserDto(userStorage.findById(user.getId()).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + user.getId() + " не найден")));
        if (user.getEmail() == null) user.setEmail(savedUser.getEmail());
        if (user.getName() == null) user.setName(savedUser.getName());
        try {
            return UserMapper.toUserDto(userStorage.save(UserMapper.toUser(user)));
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistException("Пользователь с адресом " + user.getEmail() + " уже существует");
        }
    }

    @Transactional
    public void delete(long id) {
        userStorage.deleteById(id);
    }

    public List<UserDto> getAll() {
        return userStorage.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
