package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoReturn;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

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
    private final ItemService itemService;


    @PostMapping
    public ItemDto save(@RequestHeader(User_ID) Long userId, @RequestBody ItemDto itemDto) {
        log.info("Invoke save method with user = {} and item = {}", userId, itemDto);
        return itemService.save(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(User_ID) Long userId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("Invoke update method with user = {} and item = {}", userId, itemDto);
        return itemService.update(userId, itemId, itemDto);
    }


    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(User_ID) Long userId, @PathVariable Long itemId) {
        log.info("Invoke getItemById method with user = {} and itemId = {}", userId, itemId);

        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemBookingDto> getAll(@RequestHeader(User_ID) Long userId) {
        log.info("Invoke getAll method with user = {} ", userId);
        return itemService.getAll(userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItemById(@RequestHeader(User_ID) Long userId, @PathVariable Long itemId) {
        log.info("Invoke deleteItemById method with user = {} and itemId = {}", userId, itemId);
        itemService.deleteItemById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(User_ID) Long userId, @RequestParam(name = "text") String text) {
        log.info("Invoke search method with user = {} and text = {}", userId, text);
        return itemService.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoReturn addComment(@RequestHeader(User_ID) Long userId,
                                       @RequestBody CommentDto commentDto,
                                       @PathVariable Long itemId) {
        log.info("Invoke saveComment method with user = {}, comment = {} and text = {}", userId, commentDto, itemId);
        return itemService.addComment(userId, commentDto, itemId);

    }

}
