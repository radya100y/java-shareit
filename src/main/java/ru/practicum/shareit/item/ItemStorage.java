package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import java.util.List;

public interface ItemStorage {

    Item save(Item item);

    Item get(long id);

    Item update(Item item);

    void delete(long id);

    List<Item> getAll(long userId);

    List<Item> search(String query);
}
