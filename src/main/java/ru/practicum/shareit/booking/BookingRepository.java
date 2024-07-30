package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdAndStatus(Long userId, String text);

    List<Booking> findAllByBookerId(Long userId);

    List<Booking> findAllByItemIdInAndStatus(List<Long> items, String text);

    List<Booking> findAllByItemIdIn(List<Long> items);
}
