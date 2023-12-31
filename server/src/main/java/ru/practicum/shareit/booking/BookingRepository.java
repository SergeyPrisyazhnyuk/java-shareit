package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.dto.BookingDtoInterface;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.item_id as bookingItemId, b.booker_id as bookingBookerId, b.status as bookingStatus, i.name as bookingItemName " +
            " from bookings as b join items as i on i.id = b.item_id " +
            " where b.booker_id = :bookerId " +
            " order by b.start_date desc ", nativeQuery = true)
    Page<BookingDtoInterface> findAllBookingsByBookerId(Long bookerId, Pageable pageable);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = :bookerId " +
            " and :localDateTime between b.start_date and b.end_date " +
            " order by b.start_date desc ", nativeQuery = true)
    Page<BookingDtoInterface> findCurrentBookingsByBookerId(Long bookerId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = :bookerId " +
            " and b.end_date < :localDateTime " +
            " order by b.start_date desc ", nativeQuery = true)
    Page<BookingDtoInterface> findPastBookingsByBookerId(Long bookerId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = :bookerId " +
            " and b.start_date > :localDateTime " +
            " order by b.start_date desc ", nativeQuery = true)
    Page<BookingDtoInterface> findFutureBookingsByBookerId(Long bookerId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = :bookerId " +
            " and b.status = 'WAITING'" +
            " order by b.start_date desc ", nativeQuery = true)
    Page<BookingDtoInterface> findWaitingBookingsByBookerId(Long bookerId, Pageable pageable);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = :bookerId " +
            " and b.status = 'REJECTED' " +
            " order by b.start_date desc", nativeQuery = true)
    Page<BookingDtoInterface> findRejectedBookingsByBookerId(Long bookerId, Pageable pageable);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.item_id as bookingItemId, b.booker_id as bookingBookerId, b.status as bookingStatus, i.name as bookingItemName " +
            " from public.bookings as b join public.items as i on i.id = b.item_id " +
            " where i.owner_id = :ownerId " +
            " order by b.start_date desc ", nativeQuery = true)
    List<BookingDtoInterface> findAllBookingsByOwnerId(Long ownerId, Pageable pageable);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where i.owner_id = :ownerId " +
            " and :localDateTime between b.start_date and b.end_date " +
            " order by b.start_date desc ", nativeQuery = true)
    Page<BookingDtoInterface> findCurrentBookingsByOwnerId(Long ownerId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where i.owner_id = :ownerId " +
            " and b.end_date < :localDateTime " +
            " order by b.start_date desc ", nativeQuery = true)
    Page<BookingDtoInterface> findPastBookingsByOwnerId(Long ownerId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where i.owner_id = :ownerId " +
            " and b.start_date > :localDateTime " +
            " order by b.start_date desc ", nativeQuery = true)
    Page<BookingDtoInterface> findFutureBookingsByOwnerId(Long ownerId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where i.owner_id = :ownerId " +
            " and b.status = 'WAITING'" +
            " order by b.start_date desc ", nativeQuery = true)
    Page<BookingDtoInterface> findWaitingBookingsByOwnerId(Long ownerId, Pageable pageable);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where i.owner_id = :ownerId " +
            " and b.status = 'REJECTED' " +
            " order by b.start_date desc", nativeQuery = true)
    Page<BookingDtoInterface> findRejectedBookingsByOwnerId(Long ownerId, Pageable pageable);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where b.item_id = :itemId and b.start_date < :localDateTime " +
            " and b.status = 'APPROVED' " +
            " order by b.start_date desc limit 1 ", nativeQuery = true)
    Optional<BookingDtoInterface> findLastBooking(Long itemId, LocalDateTime localDateTime);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where b.item_id = :itemId and b.start_date > :localDateTime " +
            " and b.status = 'APPROVED' " +
            " order by b.start_date asc limit 1 ", nativeQuery = true)
    Optional<BookingDtoInterface> findNextBooking(Long itemId, LocalDateTime localDateTime);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where b.status = 'APPROVED' " +
            " order by b.start_date asc ", nativeQuery = true)
    List<BookingDtoInterface> findAllByItemInAndStatusOrderByStartAsc(List<Item> items, BookingStatus bookingStatus);

    @Query(value = "select b.id as bookingId, b.start_date as bookingStartDate, b.end_date as bookingEndDate, " +
            " b.status as bookingStatus, i.id as bookingItemId, i.name as bookingItemName, b.booker_id as bookingBookerId " +
            " from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = :userId and b.item_id = :itemId and b.end_date < :localDateTime " +
            " and b.status = 'APPROVED' order by b.id desc", nativeQuery = true)
    List<BookingDtoInterface> findAllByUserAndItem(Long userId, Long itemId, LocalDateTime localDateTime);


}
