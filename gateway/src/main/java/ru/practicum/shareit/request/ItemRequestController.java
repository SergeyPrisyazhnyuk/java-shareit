package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoReturn;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String User_ID = "X-Sharer-User-Id";
    private final ItemRequsetClient itemRequsetClient;

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader(User_ID) Long userId,
                                     @Valid @RequestBody ItemRequestDto itemRequestDto) {

        log.info("Invoke save method with user = {} and itemRequest = {}", userId, itemRequestDto);
        return itemRequsetClient.save(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestByUserId(@RequestHeader(User_ID) Long userId) {

        log.info("Invoke getRequestByUserId method with user = {}", userId);
        return itemRequsetClient.getRequestByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsFromToSize(@RequestHeader(User_ID) Long userId,
                                                           @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                           @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size


                                                               ) {

        log.info("Invoke getAllRequestsFromToSize method with user = {}, from = {}, size = {}", userId, from, size);
        return itemRequsetClient.getAllRequestsFromToSize(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(User_ID) Long userId,
                                               @PathVariable Long requestId) {

        log.info("Invoke getRequestById method with user = {} and request = {}", userId, requestId);
        return itemRequsetClient.getRequestById(userId, requestId);
    }

}
