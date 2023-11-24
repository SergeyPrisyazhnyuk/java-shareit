package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;

import java.util.List;

public interface BookingService {

    BookingDtoReturn save(Long userId, BookingDto bookingDto);

    BookingDtoReturn updateBookingStatus(Long userId, Long bookingId, Boolean status);

    BookingDtoReturn getBookingByUserId(Long userId, Long bookingId);

    List<BookingDtoReturn> getAll(Long userId, String state);

    List<BookingDtoReturn> getAllByOwner(Long userId, String state);

}
