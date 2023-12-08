package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private static final String User_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoReturn save(@RequestHeader(User_ID) Long userId, @Valid @RequestBody BookingDto bookingDto) {
        log.info("Invoke save method with user = {} and booking = {}", userId, bookingDto);
        return bookingService.save(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoReturn update(@RequestHeader(User_ID) Long userId,
                                   @PathVariable("bookingId") Long bookingId,
                                   @RequestParam(name = "approved") Boolean approved
    ) {
        log.info("Invoke update method with userId = {} and bookingId = {} and status = {}  ", userId, bookingId, approved);
        return bookingService.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoReturn getBookingByUserId(@RequestHeader(User_ID) Long userId,
                                               @PathVariable("bookingId") Long bookingId
    ) {
        log.info("Invoke get method with userId = {} and bookingId = {} ", userId, bookingId);
        return bookingService.getBookingByUserId(userId, bookingId);
    }


    @GetMapping
    public List<BookingDtoReturn> getAll(@RequestHeader(User_ID) Long bookerId,
                                         @RequestParam(name = "state", defaultValue = "ALL") String state,
                                         @RequestParam(name = "from", defaultValue = "0") @Validated @Min(0) Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") @Validated @Min(1) Integer size
    ) {
        log.info("Invoke getAll method with userId = {} and state = {} and from = {} and size = {} ", bookerId, state, from, size);
        return bookingService.getAll(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoReturn> getAllByOwner(@RequestHeader(User_ID) Long ownerId,
                                         @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                @RequestParam(name = "from", defaultValue = "0") @Validated @Min(0) Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") @Validated @Min(1) Integer size
    ) {
        log.info("Invoke getAllByOwner method with userId = {} and state = {} and from = {} and size = {} ", ownerId, state, from, size);

        return bookingService.getAllByOwner(ownerId, state, from, size);

    }

}
