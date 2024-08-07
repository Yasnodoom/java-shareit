package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
        Mockito
                .when(userRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        user = new User();
        user.setId(10L);
        user.setEmail("Power@max.ru");
        user.setName("Max Power");

        userDto = UserMapper.mapToUserDto(user);
    }

    @Test
    void create() {
        UserDto result = userService.create(userDto);
        Assertions.assertNotNull(result);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void update() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));

        UserDto result = userService.update(userDto, 10L);
        Assertions.assertNotNull(result);
        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    void getAll() {
        List<User> posts = new ArrayList<>();
        posts.add(user);
        when(userRepository.findAll()).thenReturn(posts);
        List<UserDto> result = userService.getAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }
}
