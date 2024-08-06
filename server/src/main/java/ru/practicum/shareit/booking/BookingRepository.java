package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.BookingDatesDto;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdAndStatus(Long userId, String text);

    List<Booking> findAllByBookerId(Long userId);

    List<Booking> findAllByItemIdInAndStatus(List<Long> items, String text);

    List<Booking> findAllByItemIdIn(List<Long> items);

    List<Booking> findAllByItemId(Long itemId);

    @Query(value = "select b.booking_id, b.start_date, b.end_date, b.booker_id from bookings b " +
            "where b.status = 'APPROVED' " +
            "and b.end_date < current_timestamp " +
            "and b.item_id = ?1 " +
            "order by b.end_date desc " +
            "limit 1", nativeQuery = true)
    Optional<BookingDatesDto> findLastBookingByItemId(Long itemId);

    @Query(value = "select b.booking_id, b.start_date, b.end_date, b.booker_id from bookings b " +
            "where b.status = 'APPROVED' " +
            "and b.end_date > current_timestamp " +
            "and b.item_id = ?1 " +
            "order by b.end_date desc " +
            "limit 1", nativeQuery = true)
    Optional<BookingDatesDto> findNextBookingByItemId(Long itemId);
}
