package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select b.* from bookings b join items i on b.item_id = i.id " +
            " where b.booker_id = ?1 " +
            " order by b.start_date desc ", nativeQuery = true)
    List<Booking> findAllBookingsByBookerId(Long bookerId);

    @Query(value = "select b.* from bookings b join items i on b.item_id = i.id " +
            " where b.booker_id = ?1 " +
            " and ?2 between b.start_date and b.end_date " +
            " order by b.start_date desc ", nativeQuery = true)
    List<Booking> findCurrentBookingsByBookerId(Long bookerId, LocalDateTime localDateTime);

    @Query(value = "select b.* from bookings b join items i on b.item_id = i.id " +
            " where b.booker_id = ?1 " +
            " and b.end_date < ?2 " +
            " order by b.start_date desc ", nativeQuery = true)
    List<Booking> findPastBookingsByBookerId(Long bookerId, LocalDateTime localDateTime);

    @Query(value = "select b.* from bookings b join items i on b.item_id = i.id " +
            " where b.booker_id = ?1 " +
            " and b.start_date > ?2 " +
            " order by b.start_date desc ", nativeQuery = true)
    List<Booking> findFutureBookingsByBookerId(Long bookerId, LocalDateTime localDateTime);

    @Query(value = "select b.* from bookings b join items i on b.item_id = i.id " +
            " where b.booker_id = ?1 " +
            " and b.status = 'WAITING'" +
            " order by b.start_date desc ", nativeQuery = true)
    List<Booking> findWaitingBookingsByBookerId(Long bookerId);

    @Query(value = "select b.* from bookings b join items i on b.item_id = i.id " +
            " where b.booker_id = ?1 " +
            " and b.status = 'REJECTED' " +
            " order by b.start_date desc", nativeQuery = true)
    List<Booking> findRejectedBookingsByBookerId(Long bookerId);

    @Query(value = "select b.* from bookings b join items i on b.item_id = i.id " +
            " where i.owner_id = ?1 " +
            " order by b.start_date desc ", nativeQuery = true)
    List<Booking> findAllBookingsByOwnerId(Long ownerId);

    @Query(value = "select b.* from bookings b join items i on b.item_id = i.id " +
            " where i.owner_id = ?1 " +
            " and ?2 between b.start_date and b.end_date " +
            " order by b.start_date desc ", nativeQuery = true)
    List<Booking> findCurrentBookingsByOwnerId(Long ownerId, LocalDateTime localDateTime);

    @Query(value = "select b.* from bookings b join items i on b.item_id = i.id " +
            " where i.owner_id = ?1 " +
            " and b.end_date < ?2 " +
            " order by b.start_date desc ", nativeQuery = true)
    List<Booking> findPastBookingsByOwnerId(Long ownerId, LocalDateTime localDateTime);

    @Query(value = "select b.* from bookings b join items i on b.item_id = i.id " +
            " where i.owner_id = ?1 " +
            " and b.start_date > ?2 " +
            " order by b.start_date desc ", nativeQuery = true)
    List<Booking> findFutureBookingsByOwnerId(Long ownerId, LocalDateTime localDateTime);

    @Query(value = "select b.* from bookings b join items i on b.item_id = i.id " +
            " where i.owner_id = ?1 " +
            " and b.status = 'WAITING'" +
            " order by b.start_date desc ", nativeQuery = true)
    List<Booking> findWaitingBookingsByOwnerId(Long ownerId);

    @Query(value = "select b.* from bookings b join items i on b.item_id = i.id " +
            " where i.owner_id = ?1 " +
            " and b.status = 'REJECTED' " +
            " order by b.start_date desc", nativeQuery = true)
    List<Booking> findRejectedBookingsByOwnerId(Long ownerId);


    @Query(value = "select * from bookings b join items i on i.id = b.item_id " +
            " where b.item_id = ?1 and b.start_date < ?2 " +
            " and b.status = 'APPROVED' " +
            " order by b.start_date desc limit 1 ", nativeQuery = true)
    Optional<Booking> findLastBooking(Long itemId, LocalDateTime localDateTime);

    @Query(value = "select * from bookings b join items i on i.id = b.item_id " +
            " where b.item_id = ?1 and b.start_date > ?2 " +
            " and b.status = 'APPROVED' " +
            " order by b.start_date asc limit 1 ", nativeQuery = true)
    Optional<Booking> findNextBooking(Long itemId, LocalDateTime localDateTime);

    List<Booking> findAllByItemInAndStatusOrderByStartAsc(List<Item> items, BookingStatus bookingStatus);

    @Query(value = "select b.* from bookings b join items i on i.id = b.item_id " +
            " where b.booker_id = ?1 and b.item_id = ?2 and b.end_date < ?3 " +
            " and b.status = 'APPROVED' order by b.id desc", nativeQuery = true)
    @Transactional
    List<Booking> findAllByUserAndItem(Long userId, Long itemId, LocalDateTime localDateTime);

}
