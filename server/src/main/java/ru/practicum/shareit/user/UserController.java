package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto save(@Valid @RequestBody UserDto userDto) {
        log.info("Invoke save method with user = {}", userDto);
        return userService.save(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        log.info("Invoke update method with user = {}", userDto);
        return userService.update(id, userDto);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) {
        log.info("Invoke get method with user id = {}", id);
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Invoke getAll method");
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") Long id) {
        log.info("Invoke delete method with user id = {}", id);
        userService.deleteUserById(id);
    }



}
