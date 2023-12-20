package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoReturn;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private static final String User_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;


    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader(User_ID) Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Invoke save method with user = {} and item = {}", userId, itemDto);
        return itemClient.save(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(User_ID) Long userId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("Invoke update method with user = {} and item = {}", userId, itemDto);
        return itemClient.update(userId, itemId, itemDto);
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(User_ID) Long userId, @PathVariable Long itemId) {
        log.info("Invoke getItemById method with user = {} and itemId = {}", userId, itemId);

        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(User_ID) Long userId) {
        log.info("Invoke getAll method with user = {} ", userId);
        return itemClient.getAll(userId);
    }

/*
    @DeleteMapping("/{itemId}")
    public void deleteItemById(@RequestHeader(User_ID) Long userId, @PathVariable Long itemId) {
        log.info("Invoke deleteItemById method with user = {} and itemId = {}", userId, itemId);
        itemClient.deleteItemById(userId, itemId);
    }
*/

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(User_ID) Long userId, @RequestParam(name = "text") String text) {
        log.info("Invoke search method with user = {} and text = {}", userId, text);
        return itemClient.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(User_ID) Long userId,
                                       @Valid @RequestBody CommentDto commentDto,
                                       @PathVariable Long itemId) {
        log.info("Invoke saveComment method with user = {}, comment = {} and text = {}", userId, commentDto, itemId);
        return itemClient.addComment(userId, commentDto, itemId);

    }

}
