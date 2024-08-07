package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRepositoryTest {
    private final ItemRepository itemRepository;

    private Booking booking;
    private Item item;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("Power@max.ru");
        user.setName("Max Power");

        item = new Item();
        item.setName("item name");
        item.setAvailable(true);
        item.setRequestId(1L);

        booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
    }

    @Test
    void create() {
        itemRepository.save(item);
        long result = itemRepository.count();
        Assertions.assertEquals(1, result);
    }

    @Test
    void delete() {
        long id = itemRepository.save(item).getId();
        itemRepository.deleteById(id);
        long result = itemRepository.count();
        Assertions.assertEquals(0, result);
    }

    @Test
    void get() {
        long id = itemRepository.save(item).getId();
        Optional<Item> b = itemRepository.findById(id);
        Assertions.assertTrue(b.isPresent());
    }
}
