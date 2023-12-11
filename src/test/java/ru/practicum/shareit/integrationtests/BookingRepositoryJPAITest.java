package ru.practicum.shareit.integrationtests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.BookingRepository;

@DataJpaTest
class BookingRepositoryJPAITest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void findAllBookingsByBookerId() {
    }

    @Test
    void findCurrentBookingsByBookerId() {
    }

    @Test
    void findPastBookingsByBookerId() {
    }

    @Test
    void findFutureBookingsByBookerId() {
    }

    @Test
    void findWaitingBookingsByBookerId() {
    }

    @Test
    void findRejectedBookingsByBookerId() {
    }

    @Test
    void findAllBookingsByOwnerId() {
    }

    @Test
    void findCurrentBookingsByOwnerId() {
    }

    @Test
    void findPastBookingsByOwnerId() {
    }

    @Test
    void findFutureBookingsByOwnerId() {
    }

    @Test
    void findWaitingBookingsByOwnerId() {
    }

    @Test
    void findRejectedBookingsByOwnerId() {
    }

    @Test
    void findLastBooking() {
    }

    @Test
    void findNextBooking() {
    }

    @Test
    void findAllByItemInAndStatusOrderByStartAsc() {
    }

    @Test
    void findAllByUserAndItem() {
    }

}