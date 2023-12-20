package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

public interface BookingDtoInterface {

    Long getBookingId();

    LocalDateTime getBookingStartDate();

    LocalDateTime getBookingEndDate();

    BookingStatus getBookingStatus();

    Long getBookingItemId();

    Long getBookingBookerId();

    String getBookingItemName();
}
