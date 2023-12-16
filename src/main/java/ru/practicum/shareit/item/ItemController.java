package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable("id") long id) {
        return itemService.get(id);
    }

    @PostMapping()
    public ItemDto save(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.save(item, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody Item item, @RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") long itemId) {
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
