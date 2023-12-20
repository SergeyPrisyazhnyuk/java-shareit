package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody UserDto userDto) {
        log.info("Invoke save method with user = {}", userDto);
        return userClient.save(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        log.info("Invoke update method with user = {}", userDto);
        return userClient.update(id, userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Long id) {
        log.info("Invoke get method with user id = {}", id);
        return userClient.getUserById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Invoke getAll method");
        return userClient.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable("id") Long id) {
        log.info("Invoke delete method with user id = {}", id);
        return userClient.deleteUserById(id);
    }

}
