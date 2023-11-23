package ru.practicum.shareit.booking;

import java.util.Arrays;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState getState(String bookingState) {
        return Arrays.stream(BookingState.values())
                .filter(s -> s.name().equals(bookingState))
                .findFirst()
                .orElse(BookingState.ALL);
    }
}
