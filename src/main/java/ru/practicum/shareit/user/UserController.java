package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") long id) {
        return userService.get(id);
    }

    @PostMapping()
    public User save(@Valid @RequestBody User user) {
        return userService.save(user);
    }

    @PatchMapping("/{id}")
    public User update(@RequestBody User user, @PathVariable("id") long userId) {
        user.setId(userId);
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long userId) {
        userService.delete(userId);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }
}
