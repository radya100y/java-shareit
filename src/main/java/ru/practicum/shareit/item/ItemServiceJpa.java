package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.AccessException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class ItemServiceJpa implements ItemService {

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final UserRepository userRepository;

    @Override
    public ItemDto save(ItemDto item, long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));

        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(item)));
    }

    @Override
    public ItemDto get(long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) throw new NotFoundException("Вещь с идентификатором " + id + " не найдена");
        return ItemMapper.toItemDto(item.get());
    }

    @Override
    public ItemDto update(ItemDto item, long userId) {
        if (userRepository.findById(userId).isEmpty())
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден");
        if (item.getOwner() != userId)
            throw new AccessException("Пользователь " + userId + " не владелец вещи " + item.getId());

        ItemDto savedItem = get(item.getId());
        if (item.getAvailable() == null) item.setAvailable(savedItem.getAvailable());
        if (item.getName() == null) item.setName(savedItem.getName());
        if (item.getDescription() == null) item.setDescription(savedItem.getDescription());

        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(item)));
    }

    @Override
    public void delete(long id, long userId) {
        if (userRepository.findById(userId).isEmpty())
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден");
        if (get(id).getOwner() != userId)
            throw new AccessException("Пользователь " + userId + " не владелец вещи " + id);
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> getAll(long userId) {
        return itemRepository.findAllByOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String query) {
        return itemRepository.getByNameOrDescrAndAvail(query).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
