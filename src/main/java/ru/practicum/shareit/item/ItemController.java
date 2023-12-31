package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable("id") long id) {
        return itemService.get(id);
    }

    @PostMapping()
    public ItemDto save(@Valid @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.save(item, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto item,
                          @RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable("id") long itemId) {
        item.setId(itemId);
        return itemService.update(item, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        itemService.delete(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }
}
