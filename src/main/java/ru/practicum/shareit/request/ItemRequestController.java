package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoReturn save(@RequestHeader(User_ID) Long userId,
                                     @Valid @RequestBody ItemRequestDto itemRequestDto) {

        log.info("Invoke save method with user = {} and itemRequest = {}", userId, itemRequestDto);
        return itemRequestService.save(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDtoReturn> getRequestByUserId(@RequestHeader(User_ID) Long userId) {

        log.info("Invoke getRequestByUserId method with user = {}", userId);
        return itemRequestService.getRequestByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoReturn> getAllRequestsFromToSize(@RequestHeader(User_ID) Long userId,
                                                               @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                               @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size


                                                               ) {

        log.info("Invoke getAllRequestsFromToSize method with user = {}, from = {}, size = {}", userId, from, size);
        return itemRequestService.getAllRequestsFromToSize(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoReturn getRequestById(@RequestHeader(User_ID) Long userId,
                                               @PathVariable Long requestId) {

        log.info("Invoke getRequestById method with user = {} and request = {}", userId, requestId);
        return itemRequestService.getRequestById(userId, requestId);
    }

}
