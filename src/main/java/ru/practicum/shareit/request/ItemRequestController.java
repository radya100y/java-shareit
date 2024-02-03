package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    @Autowired
    private final ItemRequestService itemRequestService;

    @PostMapping()
    public ItemRequestDtoOut save(@Valid @RequestBody ItemRequestDtoIn itemRequestDtoIn,
                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        itemRequestDtoIn.setUserId(userId);
        return itemRequestService.save(itemRequestDtoIn);
    }

    @GetMapping()
    public List<ItemRequestDtoOut> getOwnRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getAllByAuthor(userId);
    }

    @GetMapping("/{id}")
    public ItemRequestDtoOut getRequest(@PathVariable("id") Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getById(id, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(defaultValue = "0") @Min(0) int from,
                                                  @RequestParam(defaultValue = "1") @Min(1) int size) {
        Pageable reqPage = PageRequest.of(from / size, size, Sort.by("id").descending());
        return itemRequestService.getAll(userId, reqPage);
    }
}
