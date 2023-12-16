package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item save(Item item, long userId);

    Item get(long id);

    Item update(Item item, long userId);

    void delete(long id, long userId);

    List<Item> getall(long userId);

    List<Item> search(String query);
}
