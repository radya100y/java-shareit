package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Repository
public class ItemStorageRam implements ItemStorage {

    private final List<Item> items;
    private long id = 0L;

    public ItemStorageRam(List<Item> items) {
        this.items = items;
    }

    @Override
    public Item save(Item item) {
        item.setId(++id);
        items.add(item);
        return item;
    }

    @Override
    public Item get(long id) {
        for (Item item : items) {
            if (item.getId() == id) return item;
        }
        throw new NotFoundException("Item " + id + " not found");
    }

    @Override
    public Item update(Item item) {
        for (Item itemFind : items) {
            if (itemFind.getId() == item.getId()) {
                if (item.getName() != null) itemFind.setName(item.getName());
                if (item.getDescription() != null) itemFind.setDescription(item.getDescription());
                if (item.getAvailable() != null) itemFind.setAvailable(item.getAvailable());
                return itemFind;
            }
        }
        throw new NotFoundException("Item " + id + " not found");
    }

    @Override
    public void delete(long id) {
        for (Item item : items) {
            if (item.getId() == id) {
                items.remove(item);
                return;
            }
        }
        throw new NotFoundException("Item " + id + " not found");
    }

    @Override
    public List<Item> getAll(long userId) {
        return items.stream()
                .filter(x -> x.getOwner() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String query) {
        return items.stream()
                .filter(Item::getAvailable)
                .filter(x -> (x.getName().toLowerCase(Locale.ROOT).contains(query) ||
                        (x.getDescription().toLowerCase(Locale.ROOT).contains(query))))
                .collect(Collectors.toList());
    }
}
