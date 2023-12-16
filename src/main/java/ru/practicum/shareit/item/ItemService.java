package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto save(Item item, long userId);

    ItemDto get(long id);

    ItemDto update(Item item, long userId);

    void delete(long id, long userId);

    List<ItemDto> getAll(long userId);

    List<ItemDto> search(String query);
}
