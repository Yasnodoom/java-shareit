package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static java.util.Collections.singletonList;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingRepositoryTest {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User user;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("Power@max.ru");
        user.setName("Max Power");

        item = new Item();
        item.setName("item name");

        booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);

        itemRepository.save(item);
        userRepository.save(user);

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void create() {
        bookingRepository.save(booking);
        long result = bookingRepository.count();
        Assertions.assertEquals(1, result);
    }

    @Test
    void delete() {
        long id = bookingRepository.save(booking).getId();
        bookingRepository.deleteById(id);
        long result = bookingRepository.count();
        Assertions.assertEquals(0, result);
    }

    @Test
    void get() {
        long id = bookingRepository.save(booking).getId();
        Optional<Booking> b = bookingRepository.findById(id);
        Assertions.assertTrue(b.isPresent());
    }

    @Test
    void findAllByItemIdIn() {
        bookingRepository.save(booking);
        int size = bookingRepository.findAllByItemIdIn(singletonList(item.getId())).size();
        Assertions.assertEquals(1, size);
    }

}
