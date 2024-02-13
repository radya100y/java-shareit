package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService service;

    private final User owner = User.builder().id(1L).email("qwe@qwe.qwe").name("qwe").build();
    private final User booker = User.builder().id(2L).email("wer@wer.wer").name("wer").build();
    private final Item item = Item.builder().id(1L).name("qwe").description("qwe").available(true).owner(1L).build();

    private final BookingDto bookingIn = BookingDto.builder().itemId(1L).userId(2L).status(BookingStatus.WAITING)
            .start(LocalDateTime.now().plus(10, ChronoUnit.HOURS))
            .end(LocalDateTime.now().plus(100, ChronoUnit.HOURS)).build();

    @Test
    @DisplayName("добавление брони")
    void shouldSave() throws Exception {
        when(service.save(any(BookingDto.class))).thenReturn(BookingMapper.toBooking(bookingIn, item, booker));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingIn))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(BookingStatus.WAITING.name())));
    }

    @Test
    @DisplayName("получение брони")
    void shouldGet() throws Exception {
        when(service.getCertain(anyLong(), anyLong())).thenReturn(BookingMapper.toBooking(bookingIn, item, booker));

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(BookingStatus.WAITING.name())));
    }

    @Test
    @DisplayName("обновление брони")
    void shouldUpdate() throws Exception {
        bookingIn.setStatus(BookingStatus.APPROVED);
        when(service.update(anyLong(), anyLong(), any(BookingStatus.class)))
                .thenReturn(BookingMapper.toBooking(bookingIn, item, booker));

        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(BookingStatus.APPROVED.name())));

        mvc.perform(patch("/bookings/1")
                        .param("approved", "false")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(BookingStatus.APPROVED.name())));

        mvc.perform(patch("/bookings/1")
                        .param("approved", "error")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("получение бронирований броневиком )")
    void shouldGetForBooker() throws Exception {
        when(service.getBookingForBooker(anyLong(), any(BookingStatusParam.class), any(Pageable.class)))
                .thenReturn(List.of(BookingMapper.toBooking(bookingIn, item, booker)));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "notExistsParam")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("получение бронирований владельцем")
    void shouldGetForOwner() throws Exception {
        when(service.getBookingForOwner(anyLong(), any(BookingStatusParam.class), any(Pageable.class)))
                .thenReturn(List.of(BookingMapper.toBooking(bookingIn, item, booker)));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
