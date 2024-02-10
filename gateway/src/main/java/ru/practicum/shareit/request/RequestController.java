package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestIn;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping()
    public ResponseEntity<Object> save(@Valid @RequestBody RequestIn request,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.addRequest(request, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getOwnRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getRequestsOwned(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequest(@PathVariable("id") long id,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getRequest(id, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(defaultValue = "1") @Min(1) Integer size) {
        return requestClient.getRequests(userId, from, size);
    }
}
