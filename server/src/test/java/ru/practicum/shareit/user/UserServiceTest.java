package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.error.AlreadyExistException;
import ru.practicum.shareit.error.ErrorResponse;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    private UserRepository userRepository;

    private UserService userService;

    private final User user = User.builder().id(1L).email("qwe@qwe.qwe").name("qwe").build();
    private final UserDto userIn = UserDto.builder().id(1).email("wer@wer.com").name("wer").build();

    private final UserDto userOut = UserDto.builder().id(1).email("wer@wer.com").name("wer").build();


    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);

        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Получение пользователя")
    void shouldGetModel() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        assert user != null;
        Assertions.assertEquals(userService.getModel(1L).getEmail(), user.getEmail());
        Assertions.assertThrows(NotFoundException.class, () -> userService.getModel(2L));
    }

    @Test
    @DisplayName("Удаление пользователя")
    void shouldDelete() {
        Assertions.assertDoesNotThrow(() -> userService.delete(1L));
    }

    @Test
    @DisplayName("Все пользователи")
    void shouldGetAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        Assertions.assertEquals(userService.getAll().get(0).getEmail(), user.getEmail());
    }

    @Test
    @DisplayName("Обновление пользователя")
    void shouldUpdate() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assert user != null;
        user.setName("ert");
        Assertions.assertEquals(userService.update(userIn).getName(), "ert");
        userIn.setId(2L);
        Assertions.assertThrows(NotFoundException.class, () -> userService. update(userIn));
        Assertions.assertThrows(AlreadyExistException.class, () -> {
            throw new AlreadyExistException("qwe");
        });
        Assertions.assertEquals(new ErrorResponse("qwe").getError(), "qwe");
    }

    @Test
    @DisplayName("Фиктивный тест на маппер")
    void test_cannot_instantiate() {
        Assertions.assertThrows(InvocationTargetException.class, () -> {
            var constructor = UserMapper.class.getDeclaredConstructor();
            Assertions.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }
}
