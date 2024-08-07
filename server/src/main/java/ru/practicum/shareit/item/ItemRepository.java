package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long userId);

    List<Item> findByDescription(String text);

    List<Item> findByNameIgnoreCase(String name);

    @Query(value = "select * from items i where i.request_id = ?1",
            nativeQuery = true)
    List<Item> findByRequestId(Long requestId);
}
