package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto save(ItemDto item, long userId);

    ItemDto get(long id, long userId);

    ItemDto update(ItemDto item, long userId);

    void delete(long id, long userId);

    List<ItemDto> getAll(long userId);

    List<ItemDto> search(String query);
}
