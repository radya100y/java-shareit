package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserIn;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        return userClient.getUser(id);
    }

    @PostMapping()
    public ResponseEntity<Object> save(@Valid @RequestBody UserIn user) {
        return userClient.postUser(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody UserIn user, @PathVariable("id") Long userId) {
        return userClient.patchUser(userId, user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long userId) {
        userClient.deleteUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.getUsers();
    }
}
