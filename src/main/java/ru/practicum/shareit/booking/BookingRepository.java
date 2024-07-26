package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUserIdAndState(Long userId, String text);

    List<Booking> findAllByItemsAndState(List<Item> items, String text);
}
