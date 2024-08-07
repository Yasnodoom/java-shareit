package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ItemServiceTest {
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;

    private ItemService itemService;
    private UserService userService;
    private BookingService bookingService;

    private Item item;
    private ItemDto itemDto;
    private User user;
    private Comment comment;
    private Booking booking;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        commentRepository = mock(CommentRepository.class);

        userService = new UserService(userRepository);
        itemService = new ItemService(itemRepository, userService, commentRepository, bookingRepository);
        bookingService = new BookingService(bookingRepository, itemRepository, itemService, userService);

        Mockito
                .when(userRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        comment = new Comment();
        comment.setId(1L);
        comment.setText("it is comment");

        user = new User(1L, "Max Power", "Power@max.ru");
        itemDto = new ItemDto(1L, "item name", "item desc", true, 1L, 1L, Collections.singletonList(comment));
        item = new Item();
        item.setId(1L);
        item.setOwner(user);
        item.setName("item");
        item.setAvailable(true);
        item.setRequestId(1L);

        booking = new Booking();
        booking.setId(1L);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
    }

    @Test
    void create() {
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Mockito
                .when(itemRepository.save(any()))
                .thenReturn(item);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));

        ItemDto result = itemService.create(itemDto, 1L);
        Assertions.assertNotNull(result);
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void getAll() {
        when(itemRepository.findByOwnerId(anyLong())).thenReturn(Collections.singletonList(item));

        List<Item> items = new ArrayList<>();
        items.add(item);
        when(itemRepository.findAll()).thenReturn(items);

        List<ItemDto> result = itemService.getAll(1L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        verify(itemRepository, times(1)).findByOwnerId(anyLong());
    }

    @Test
    void addComment() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(commentRepository.save(any())).thenReturn(comment);
        when((bookingRepository.findAllByItemId(anyLong()))).thenReturn(Collections.singletonList(booking));
        when(bookingRepository.findAllByItemId(anyLong()))
                .thenReturn(Collections.singletonList(booking));

        Comment result = itemService.addComment(1L, 1L, "comment text");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(comment.getText(), result.getText());
        verify(bookingRepository, times(2)).findAllByItemId(anyLong());
        verify(commentRepository, times(1)).save(any());
    }

}
