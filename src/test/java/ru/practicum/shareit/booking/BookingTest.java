package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    Booking booking = new Booking();

    @Test
    void testEquals() {

        booking.setId(0L);

        Booking booking1 = booking;

        boolean trueIs = booking.equals(booking1);

        assertTrue(trueIs);

    }

    @Test
    void testHashCode() {

        booking.setId(0L);

        assertTrue(booking.hashCode() != 0);

    }

    @Test
    void getId() {

        booking.setId(1L);

        assertTrue(booking.getId() != 0);

    }

    @Test
    void getStart() {

        booking.setStart(LocalDateTime.now());

        assertTrue(booking.getStart() != null);


    }

    @Test
    void getEnd() {

        booking.setEnd(LocalDateTime.now());

        assertTrue(booking.getEnd() != null);

    }

    @Test
    void getItem() {

        booking.setItem(new Item());

        assertTrue(booking.getItem() != null);

    }

    @Test
    void getBooker() {

        booking.setBooker(new User());

        assertTrue(booking.getBooker() != null);

    }

    @Test
    void getStatus() {

        booking.setStatus(BookingStatus.APPROVED);

        assertTrue(booking.getStatus() == BookingStatus.APPROVED);

    }

    @Test
    void setId() {

        booking.setId(1L);

        assertTrue(booking.getId() != 0);

    }

    @Test
    void setStart() {

        booking.setStart(LocalDateTime.now());

        assertTrue(booking.getStart() != null);

    }

    @Test
    void setEnd() {

        booking.setEnd(LocalDateTime.now());

        assertTrue(booking.getEnd() != null);

    }

    @Test
    void setItem() {

        booking.setItem(new Item());

        assertTrue(booking.getItem() != null);

    }

    @Test
    void setBooker() {

        booking.setBooker(new User());

        assertTrue(booking.getBooker() != null);

    }

    @Test
    void setStatus() {

        booking.setStatus(BookingStatus.APPROVED);

        assertTrue(booking.getStatus() == BookingStatus.APPROVED);

    }
}