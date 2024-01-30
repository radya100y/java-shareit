package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    private final ItemDto itemOut =ItemDto.builder().name("qwe").description("qwe").available(true).owner(1L).build();

    @Test
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

}
