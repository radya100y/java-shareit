package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.AccessException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Deprecated
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto save(ItemDto item, long userId) {
        userStorage.get(userId);
        item.setOwner(userId);
        return ItemMapper.toItemDto(itemStorage.save(ItemMapper.toItem(item)));
    }

    @Override
    public ItemDto get(long id) {
        return ItemMapper.toItemDto(itemStorage.get(id));
    }

    @Override
    public ItemDto update(ItemDto item, long userId) {
        userStorage.get(userId);
        if (itemStorage.get(item.getId()).getOwner() != userId)
            throw new AccessException("User " + userId + " not owner for item " + item.getId());
        return ItemMapper.toItemDto(itemStorage.update(ItemMapper.toItem(item)));
    }

    @Override
    public void delete(long id, long userId) {
        userStorage.get(userId);
        if (itemStorage.get(id).getOwner() != userId)
            throw new AccessException("User " + userId + " not owner for item " + id);
        itemStorage.delete(id);
    }

    @Override
    public List<ItemDto> getAll(long userId) {
        return itemStorage.getAll(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String query) {
        return itemStorage.search(query).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
