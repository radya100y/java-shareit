package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    @PostMapping()
    public ItemRequestDtoOut save(@Valid @RequestBody ItemRequestDtoIn itemRequestDtoIn,
                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        return null;
    }

    @GetMapping()
    public List<ItemRequestDtoOut> getOwnRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return null;
    }

    @GetMapping("/{id}")
    public ItemRequestDtoOut getRequest(@PathVariable("id") Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return null;
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> getAllRequests(@RequestParam Long from, @RequestParam Long size) {
        return null;
    }
}
