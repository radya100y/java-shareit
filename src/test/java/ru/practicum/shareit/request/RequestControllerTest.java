package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService service;

    ItemRequestDtoIn irIn = new ItemRequestDtoIn(1L, "qwe");

    ItemRequestDtoOut irOut = new ItemRequestDtoOut(1L, "qwe",LocalDateTime.now(), List.of());

    @Test
    @DisplayName("Добавляем запрос вещи")
    void shouldSaveNew() throws Exception {
        when(service.save(any())).thenReturn(irOut);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(irIn))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(irOut.getDescription())));
    }

    @Test
    @DisplayName("Вывод запроса по ИД")
    void shouldGetById() throws Exception {
        when(service.getById(anyLong(), anyLong())).thenReturn(irOut);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(irOut.getDescription())));
    }

    @Test
    @DisplayName("Вывод запросов автора")
    void shouldGetAllOwner() throws Exception {
        when(service.getAllByAuthor(anyLong())).thenReturn(List.of(irOut));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Вывод всех запросов")
    void shouldGetAll() throws Exception {
        when(service.getAll(anyLong(), any(Pageable.class))).thenReturn(List.of(irOut));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
