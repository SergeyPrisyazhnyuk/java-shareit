package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoInterface;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    Booking booking = new Booking(1L, LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(10), new Item(), new User(), BookingStatus.APPROVED);

    BookingDtoInterface bookingDtoInterface = new BookingDtoInterface() {
        @Override
        public Long getBookingId() {
            return 1L;
        }

        @Override
        public LocalDateTime getBookingStartDate() {
            return LocalDateTime.now().plusMinutes(2);
        }

        @Override
        public LocalDateTime getBookingEndDate() {
            return LocalDateTime.now().plusMinutes(10);
        }

        @Override
        public BookingStatus getBookingStatus() {
            return BookingStatus.APPROVED;
        }

        @Override
        public Long getBookingItemId() {
            return 0L;
        }

        @Override
        public String getBookingItemName() {
            return "Item Name";
        }

        @Override
        public Long getBookingBookerId() {
            return 1L;
        }
    };

    @Test
    void toBookingDtoReturn() {
        BookingDtoReturn bookingDtoReturn = BookingMapper.bookingDtoReturn(booking);

        assertEquals(booking.getId(), bookingDtoReturn.getId());
    }

    @Test
    void bookingDtoReturnFromInterface() {
        BookingDtoReturn bookingDtoReturn = BookingMapper.bookingDtoReturnFromInterface(bookingDtoInterface);

        assertNotNull(bookingDtoReturn);
        assertEquals(1L,bookingDtoReturn.getId());
    }
}