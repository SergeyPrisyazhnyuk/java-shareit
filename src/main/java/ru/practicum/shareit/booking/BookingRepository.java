package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoInterface;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = ?1 " +
            " order by b.start_date desc ", nativeQuery = true)
    List<BookingDtoInterface> findAllBookingsByBookerId(Long bookerId);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = ?1 " +
            " and ?2 between b.start_date and b.end_date " +
            " order by b.start_date asc ", nativeQuery = true)
    List<BookingDtoInterface> findCurrentBookingsByBookerId(Long bookerId, LocalDateTime localDateTime);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = ?1 " +
            " and b.end_date < ?2 " +
            " order by b.start_date desc ", nativeQuery = true)
    List<BookingDtoInterface> findPastBookingsByBookerId(Long bookerId, LocalDateTime localDateTime);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = ?1 " +
            " and b.start_date > ?2 " +
            " order by b.start_date desc ", nativeQuery = true)
    List<BookingDtoInterface> findFutureBookingsByBookerId(Long bookerId, LocalDateTime localDateTime);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = ?1 " +
            " and b.status = 'WAITING'" +
            " order by b.start_date desc ", nativeQuery = true)
    List<BookingDtoInterface> findWaitingBookingsByBookerId(Long bookerId);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = ?1 " +
            " and b.status = 'REJECTED' " +
            " order by b.start_date desc", nativeQuery = true)
    List<BookingDtoInterface> findRejectedBookingsByBookerId(Long bookerId);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where i.owner_id = ?1 " +
            " order by b.start_date desc ", nativeQuery = true)
    List<BookingDtoInterface> findAllBookingsByOwnerId(Long ownerId);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where i.owner_id = ?1 " +
            " and ?2 between b.start_date and b.end_date " +
            " order by b.start_date desc ", nativeQuery = true)
    List<BookingDtoInterface> findCurrentBookingsByOwnerId(Long ownerId, LocalDateTime localDateTime);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where i.owner_id = ?1 " +
            " and b.end_date < ?2 " +
            " order by b.start_date desc ", nativeQuery = true)
    List<BookingDtoInterface> findPastBookingsByOwnerId(Long ownerId, LocalDateTime localDateTime);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where i.owner_id = ?1 " +
            " and b.start_date > ?2 " +
            " order by b.start_date desc ", nativeQuery = true)
    List<BookingDtoInterface> findFutureBookingsByOwnerId(Long ownerId, LocalDateTime localDateTime);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where i.owner_id = ?1 " +
            " and b.status = 'WAITING'" +
            " order by b.start_date desc ", nativeQuery = true)
    List<BookingDtoInterface> findWaitingBookingsByOwnerId(Long ownerId);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where i.owner_id = ?1 " +
            " and b.status = 'REJECTED' " +
            " order by b.start_date desc", nativeQuery = true)
    List<BookingDtoInterface> findRejectedBookingsByOwnerId(Long ownerId);

//b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status
@Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where b.item_id = ?1 and b.start_date < ?2 " +
            " and b.status = 'APPROVED' " +
            " order by b.start_date desc limit 1 ", nativeQuery = true)
    Optional<BookingDtoInterface> findLastBooking(Long itemId, LocalDateTime localDateTime);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where b.item_id = ?1 and b.start_date > ?2 " +
            " and b.status = 'APPROVED' " +
            " order by b.start_date asc limit 1 ", nativeQuery = true)
    Optional<BookingDtoInterface> findNextBooking(Long itemId, LocalDateTime localDateTime);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where b.status = 'APPROVED' " +
            " order by b.start_date asc ", nativeQuery = true)
    List<BookingDtoInterface> findAllByItemInAndStatusOrderByStartAsc(List<Item> items, BookingStatus bookingStatus);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = ?1 and b.item_id = ?2 and b.end_date < ?3 " +
            " and b.status = 'APPROVED' order by b.id desc", nativeQuery = true)
    @Transactional
    List<BookingDtoInterface> findAllByUserAndItem(Long userId, Long itemId, LocalDateTime localDateTime);


}
