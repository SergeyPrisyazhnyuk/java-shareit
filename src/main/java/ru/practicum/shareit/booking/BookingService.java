package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;

import java.util.List;

public interface BookingService {

    BookingDtoReturn save(Long userId, BookingDto bookingDto);

    BookingDto updateBookingStatus(Long userId, Long bookingId, Boolean status);

    BookingDto getBookingByUserId(Long userId, Long bookingId);

    List<BookingDto> getAll(Long userId, String state);

    List<BookingDto> getAllByOwner(Long userId, String state);

}
