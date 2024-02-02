package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.error.AccessException;
import ru.practicum.shareit.error.AlreadyExistException;
import ru.practicum.shareit.error.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService service;

    UserDto userIn = UserDto.builder().name("qwe").email("qwe@qwe.qwe").build();

    UserDto userOut = UserDto.builder().id(1L).name("qwe").email("qwe@qwe.qwe").build();

    @Test
    @DisplayName("Получение пользователя")
    void shouldGet() throws Exception {
        when(service.get(1L)).thenReturn(userOut);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(userIn.getEmail())));
    }

    @Test
    @DisplayName("Добавление пользователя")
    void shouldSave() throws Exception {
        when(service.save(any(UserDto.class))).thenReturn(userOut);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(userIn.getEmail())));

        userIn.setEmail("");

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Обновление пользователя")
    void shouldUpdate() throws Exception {
        when(service.update(any(UserDto.class))).thenReturn(userOut);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(userIn.getEmail())));
    }

    @Test
    @DisplayName("Удаление пользователя")
    void shouldDelete() throws Exception {

        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void shouldList() throws Exception {
        when(service.getAll()).thenReturn(List.of(userOut));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Проверка ошибок")
    void showError() throws Exception {
        when(service.save(any(UserDto.class))).thenThrow(new AlreadyExistException("qwe"));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        when(service.save(any(UserDto.class))).thenThrow(new AccessException("qwe"));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        when(service.save(any(UserDto.class))).thenThrow(new ValidateException("qwe"));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
