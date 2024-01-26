package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDtoOut save(ItemRequestDtoIn ir);

    List<ItemRequestDtoOut> getAllByAuthor(Long userId);

    ItemRequestDtoOut getById(Long itemRequestId, Long userId);

    List<ItemRequestDtoOut> getAll(Long userId, Pageable pageable);

    ItemRequest getModelById(Long itemRequestId, Long userId);
}
