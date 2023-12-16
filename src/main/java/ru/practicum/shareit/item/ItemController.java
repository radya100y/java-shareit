package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
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
    public Item get(@PathVariable("id") long id) {
        return itemService.get(id);
    }

    @PostMapping()
    public Item save(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.save(item, userId);
    }

    @PatchMapping("/{id}")
    public Item update(@RequestBody Item item, @RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") long itemId) {
        item.setId(itemId);
        return itemService.update(item, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        itemService.delete(itemId, userId);
    }

    @GetMapping
    public List<Item> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam String text) {
        return itemService.search(text);
    }
}
