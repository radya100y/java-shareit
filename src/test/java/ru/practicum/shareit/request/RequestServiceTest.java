package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RequestServiceTest {

    private ItemRequestRepository itemRequestRepository;

    private UserService userService;

    private ItemRequestService service;

    private ItemService itemService;

    @BeforeEach
    void setUp() {

        itemRequestRepository = mock(ItemRequestRepository.class);

        userService = mock(UserService.class);

        itemService = mock(ItemService.class);

        service = new ItemRequestServiceJpa(itemRequestRepository, userService, itemService);
    }

    @Test
    @DisplayName("Вывод запросов по автору")
    void getAllByAuthor() {

        when(userService.get(anyLong())).thenReturn(UserDto.builder().id(1L).build());
        when(itemRequestRepository.findAllByAuthor_IdOrderByIdDesc(anyLong()))
                .thenReturn(List.of(ItemRequest.builder().id(1L).build()));

        var result = service.getAllByAuthor(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.get(0).getId());
    }

    @Test
    @DisplayName("Вывод запроса по ИД")
    void getModelById() {

        ItemRequest ir = ItemRequest.builder().id(1L).build();

        when(userService.get(anyLong())).thenReturn(UserDto.builder().id(1L).build());
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.ofNullable(ir));

        var result = service.getModelById(1L, 1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertThrows(NotFoundException.class, () -> service.getModelById(2L, 1L));
    }

    @Test
    @DisplayName("Вывод запроса по ИД - вспом")
    void getById() {
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.ofNullable(ItemRequest.builder().id(1L).build()));
        when(itemService.getItemsForRequest(anyLong())).thenReturn(List.of(new ItemDto()));

        Assertions.assertDoesNotThrow(() -> service.getById(1L, 1L));
    }

    @Test
    @DisplayName("Вывод всех запросов")
    void getAll() {

        ItemRequest ir = ItemRequest.builder().id(1L).build();

        when(itemRequestRepository.findAllByAuthor_IdNot(anyLong(), any(Pageable.class)))
                .thenReturn(Collections.singletonList(ir));

        var result = service.getAll(1L, Pageable.ofSize(1));

        Assertions.assertNotNull(result);
    }

}
