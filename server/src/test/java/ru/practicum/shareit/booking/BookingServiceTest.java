package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookingServiceTest {
    private BookingRepository bookingRepository;
    private BookingService bookingService;
    private ItemRepository itemRepository;
    private ItemService itemService;
    private UserService userService;
    private UserRepository userRepository;

    private Booking booking;
    private BookingRequestDto bookingRequestDto;
    private BookingResponseDto bookingResponseDto;
    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);

        userService = new UserService(userRepository);
        itemService = new ItemService(itemRepository, userService, mock(CommentRepository.class), bookingRepository);
        bookingService = new BookingService(bookingRepository, itemRepository, itemService, userService);

        Mockito
                .when(bookingRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));


        user = new User();
        user.setId(1L);
        user.setEmail("Power@max.ru");
        user.setName("Max Power");

        item = new Item();
        item.setName("item name");
        item.setOwner(user);
        item.setAvailable(true);

        booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);

        bookingResponseDto = BookingMapper.toBookingResponseDto(booking);
        bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setBookerId(1L);
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStatus(Status.APPROVED);
    }

    @Test
    void create() {
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));


        BookingResponseDto result = bookingService.create(2L, bookingRequestDto);
        Assertions.assertNotNull(result);
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void approved() {
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.findById(any()))
                .thenReturn(Optional.ofNullable(booking));

        BookingResponseDto result = bookingService.approved(1L, 1L, true);
        Assertions.assertNotNull(result);
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void getAll() {
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.findById(any()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito
                .when(bookingRepository.findAllByBookerId(anyLong()))
                .thenReturn(singletonList(booking));

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        when(bookingRepository.findAll()).thenReturn(bookings);
        List<BookingResponseDto> result = bookingService.getAll(1L, Status.ALL);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAllByBookerId(anyLong());
    }
}
