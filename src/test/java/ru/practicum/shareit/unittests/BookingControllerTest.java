package ru.practicum.shareit.unittests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {


    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    Long bookerId = 0L;
    Long userId = 1L;
    Long bookingId = 0L;
    String state = "ALL";
    Integer from = 0;
    Integer size = 1;
    Boolean approved = true;

    @Test
    void save_whenInvoked_thenBookingsExists() {
        BookingDtoReturn expectedBookings = new BookingDtoReturn();
        BookingDto bookingDto = new BookingDto();

        Mockito.when(bookingService.save(userId, bookingDto)).thenReturn(expectedBookings);

        BookingDtoReturn booking = bookingController.save(userId, bookingDto);

        assertEquals(expectedBookings, booking);

    }

    @Test
    void update_whenInvoked_thenBookingsExists() {
        BookingDtoReturn expectedBookings = new BookingDtoReturn();

        Mockito.when(bookingService.updateBookingStatus(userId, bookingId, approved)).thenReturn(expectedBookings);

        BookingDtoReturn booking = bookingController.update(userId, bookingId, approved);

        assertEquals(expectedBookings, booking);

    }

    @Test
    void getBookingByUserId_whenInvoked_thenBookingsExists() {
        BookingDtoReturn expectedBookings = new BookingDtoReturn(bookingId, userId);

        Mockito.when(bookingService.getBookingByUserId(userId, bookingId)).thenReturn(expectedBookings);

        BookingDtoReturn booking = bookingController.getBookingByUserId(userId, bookingId);

        assertEquals(expectedBookings, booking);
        assertEquals(0L, booking.getId(), "Booking should have 0L id");
        assertEquals(1L, booking.getBookerId(), "Booking should have 1L bookerId");

    }

    @Test
    void getAll_whenInvoked_thenBookingNotExists() {

        List<BookingDtoReturn> bookingsExpected = List.of();

        Mockito.when(bookingService.getAll(bookerId, state, from, size)).thenReturn(bookingsExpected);

        List<BookingDtoReturn> bookings = bookingController.getAll(bookerId, state, from, size);

        assertTrue(bookings.isEmpty());
        assertEquals(0,bookings.size(), "Booking list should be empty");

    }

    @Test
    void getAll_whenInvoked_thenBookingsExists() {

        List<BookingDtoReturn> expectedBookings = List.of(new BookingDtoReturn(bookingId, userId));

        Mockito.when(bookingService.getAll(bookerId, state, from, size)).thenReturn(expectedBookings);

        List<BookingDtoReturn> bookings = bookingController.getAll(bookerId, state, from, size);

        assertEquals(expectedBookings, bookings);
        assertEquals(1, bookings.size(), "Booking list should not be empty");
        assertEquals(0L, bookings.get(0).getId(), "Booking should have 0L id");
        assertEquals(1L, bookings.get(0).getBookerId(), "Booking should have 1L bookerId");

    }

    @Test
    void getAllByOwner_whenInvoked_thenBookingsExists() {

        List<BookingDtoReturn> expectedBookings = List.of(new BookingDtoReturn(bookingId, userId));

        Mockito.when(bookingService.getAllByOwner(bookerId, state, from, size)).thenReturn(expectedBookings);

        List<BookingDtoReturn> bookings = bookingController.getAllByOwner(bookerId, state, from, size);

        assertEquals(expectedBookings, bookings);
        assertEquals(1, bookings.size(), "Booking list should not be empty");
        assertEquals(0L, bookings.get(0).getId(), "Booking should have 0L id");
        assertEquals(1L, bookings.get(0).getBookerId(), "Booking should have 1L bookerId");

    }


}
