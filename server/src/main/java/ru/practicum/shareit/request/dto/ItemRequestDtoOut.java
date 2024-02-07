package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@SuperBuilder
public class ItemRequestDtoOut {

    private long id;

    private String description;

    private LocalDateTime created;

    private List<ItemDto> items;
}
