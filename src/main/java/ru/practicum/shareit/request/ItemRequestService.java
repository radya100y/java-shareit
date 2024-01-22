package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDtoOut save(ItemRequestDtoIn ir);

    List<ItemRequestDtoOut> getAllByAuthor(Long userId);

    ItemRequestDtoOut getById(Long itemRequestId, Long userId);
}
