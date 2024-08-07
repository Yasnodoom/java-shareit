package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestRepositoryTest {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private ItemRequest itemRequest;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("Power@max.ru");
        user.setName("Max Power");

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDesc("desc");
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());

        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void create() {
        itemRequestRepository.save(itemRequest);
        long result = itemRequestRepository.count();
        Assertions.assertEquals(1, result);
    }

    @Test
    void delete() {
        long id = itemRequestRepository.save(itemRequest).getId();
        itemRequestRepository.deleteById(id);
        long result = itemRequestRepository.count();
        Assertions.assertEquals(0, result);
    }

    @Test
    void get() {
        long id = itemRequestRepository.save(itemRequest).getId();
        Optional<ItemRequest> b = itemRequestRepository.findById(id);
        Assertions.assertTrue(b.isPresent());
    }

    @Test
    void findAllByRequesterId() {
        itemRequestRepository.save(itemRequest);
        List<ItemRequest> b = itemRequestRepository.findAllByRequesterId(user.getId());
        Assertions.assertEquals(1, b.size());
    }

}
