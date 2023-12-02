package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoInterface;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.item.dto.ItemBookingInterfaceDto;
import ru.practicum.shareit.user.UserDto;

public class BookingMapper {

    public static BookingDtoReturn bookingDtoReturn(Booking booking) {
        return new BookingDtoReturn(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                new ItemBookingInterfaceDto(booking.getItem().getId(), booking.getItem().getName()),
                new UserDto(booking.getBooker().getId()),
                booking.getBooker().getId()
        );

    }

    public static BookingDtoReturn bookingDtoReturnFromInterface(BookingDtoInterface booking) {
        return new BookingDtoReturn(
                booking.getBookingId(),
                booking.getBookingStartDate(),
                booking.getBookingEndDate(),
                booking.getBookingStatus(),
                new ItemBookingInterfaceDto(booking.getBookingItemId(), booking.getBookingItemName()),
                new UserDto(booking.getBookingBookerId())
                ,booking.getBookingBookerId()
        );
    }

}
