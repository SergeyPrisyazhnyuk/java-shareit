package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
			@PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}


	@GetMapping("/owner")
	public ResponseEntity<Object> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
												@RequestParam(name = "state", defaultValue = "ALL") String state,
												@RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
												@RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size
	) {
		log.info("Invoke getAllByOwner method with userId = {} and state = {} and from = {} and size = {} ", ownerId, state, from, size);

		return bookingClient.getAllByOwner(ownerId, state, from, size);

	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId,
								   @PathVariable("bookingId") Long bookingId,
								   @RequestParam(name = "approved") Boolean approved
	) {
		log.info("Invoke update method with userId = {} and bookingId = {} and status = {}  ", userId, bookingId, approved);
		return bookingClient.update(userId, bookingId, approved);
	}

}
