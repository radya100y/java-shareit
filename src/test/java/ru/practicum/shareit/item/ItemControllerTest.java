package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService service;

    private final ItemDto itemIn = ItemDto.builder().name("qwe").description("qwe").available(true).owner(1L).build();

    private final CommentRequest commentIn =
            new CommentRequest(1L, 1L, "qwe");
    private final CommentResponse commentOut =
            new CommentResponse(1L, "qwe", "qwe", LocalDateTime.now());

    private final ItemDto itemOut = ItemDto.builder().name("qwe").description("qwe").available(true).owner(1L)
            .comments(List.of(commentOut)).build();


    @Test
    @DisplayName("проверяем встакку новой вещи")
    void shouldSave() throws Exception {
        when(service.save(any(ItemDto.class), anyLong())).thenReturn(itemOut);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemIn))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemIn.getDescription())));
    }

    @Test
    @DisplayName("Получение вещи")
    void shouldGetById() throws Exception {
        when(service.get(anyLong(), anyLong())).thenReturn(itemOut);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemIn.getDescription())));
    }

    @Test
    @DisplayName("Обновление вещи")
    void shouldUpdate() throws Exception {
        when(service.update(any(ItemDto.class), anyLong())).thenReturn(itemOut);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemIn))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("qwe")));
    }

    @Test
    @DisplayName("Удаление вещи")
    void shouldDelete() throws Exception {

        mvc.perform(delete("/items/10")
                .header("X-Sharer-User-Id", "1")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Получение списка вещей")
    void shouldGetList() throws Exception {
        when(service.getAll(anyLong())).thenReturn(List.of(itemOut));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Поиск вещи")
    void shouldSearch() throws Exception {
        when(service.search(anyString())).thenReturn(List.of(itemOut));

        mvc.perform(get("/items/search")
                        .param("text", "qwe")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Добавление комментария к вещи")
    void shouldAddComment() throws Exception {
        when(service.addComment(any(CommentRequest.class))).thenReturn(commentOut);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentIn))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
