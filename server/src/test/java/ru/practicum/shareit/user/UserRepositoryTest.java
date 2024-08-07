package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {
    private final UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("Power@max.ru");
        user.setName("Max Power");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void count() {
        userRepository.save(user);
        long result = userRepository.count();
        Assertions.assertEquals(1, result);
    }

    @Test
    void delete() {
        long id = userRepository.save(user).getId();
        userRepository.deleteById(id);
        long result = userRepository.count();
        Assertions.assertEquals(0, result);
    }

    @Test
    void get() {
        long id = userRepository.save(user).getId();
        Optional<User> u = userRepository.findById(id);
        Assertions.assertTrue(u.isPresent());
    }
}
