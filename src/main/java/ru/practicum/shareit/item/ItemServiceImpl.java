package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.AccessException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }


    @Override
    public Item save(Item item, long userId) {
        userStorage.get(userId);
        item.setOwner(userId);
        return itemStorage.save(item);
    }

    @Override
    public Item get(long id) {
        return itemStorage.get(id);
    }

    @Override
    public Item update(Item item, long userId) {
        userStorage.get(userId);
        if (itemStorage.get(item.getId()).getOwner() != userId)
            throw new AccessException("User " + userId + " not owner for item " + item.getId());
        return itemStorage.update(item);
    }

    @Override
    public void delete(long id, long userId) {
        userStorage.get(userId);
        if (itemStorage.get(id).getOwner() != userId)
            throw new AccessException("User " + userId + " not owner for item " + id);
        itemStorage.delete(id);
    }

    @Override
    public List<Item> getAll(long userId) {
        return itemStorage.getAll(userId);
    }

    @Override
    public List<Item> search(String query) {
        return itemStorage.search(query);
    }
}
