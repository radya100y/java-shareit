package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest toItemRequestFromIn(ItemRequestDtoIn itemRequestIn, User author) {
        return ItemRequest.builder()
                .author(author)
                .description(itemRequestIn.getDescription())
                .created(itemRequestIn.getCreated())
                .build();
    }

    public static ItemRequestDtoOut itemRequestOut(ItemRequest itemRequest, List<ItemDto> itemDtos) {
        return ItemRequestDtoOut.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemDtos)
                .build();
    }
}
