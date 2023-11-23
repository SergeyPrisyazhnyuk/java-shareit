package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

public class BookingMapper {

    public static BookingDtoReturn bookingDtoReturn(Booking booking) {
        return new BookingDtoReturn(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBookingStatus(),
                ItemMapper.toItemBookingDto(booking.getItem()),
                UserMapper.toUserDtoWithId(booking.getBooker())
        );

    }

}
