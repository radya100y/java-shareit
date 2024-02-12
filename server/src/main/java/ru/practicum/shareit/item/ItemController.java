package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable("id") long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.get(id, userId);
    }

    @PostMapping()
    public ItemDto save(@Valid @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") long userId) {
        item.setOwner(userId);
        return itemService.save(item, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto item,
                          @RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable("id") long itemId) {
        item.setId(itemId);
        item.setOwner(userId);
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
        if (text.isBlank()) return List.of();
        return itemService.search(text);
    }

    @PostMapping("/{id}/comment")
    public CommentResponse addComment(@Valid @RequestBody CommentRequest commentRequest,
                                      @RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable("id") long itemId) {
        commentRequest.setItemId(itemId);
        commentRequest.setUserId(userId);
        return itemService.addComment(commentRequest);
    }
}
