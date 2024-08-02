package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RightsException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.booking.BookingMapper.mapToBooking;
import static ru.practicum.shareit.booking.BookingMapper.toBookingResponseDto;
import static ru.practicum.shareit.booking.Status.*;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final UserService userService;

    public BookingResponseDto get(Long userId, Long bookingId) {
        Booking booking = findBookingById(bookingId);

        if (!booking.getBooker().getId().equals(userId)
                || !booking.getItem().getOwner().getId().equals(userId)) {
            return BookingMapper.toBookingResponseDto(booking);
        }
        throw new RightsException();
    }

    public BookingResponseDto create(Long userId, BookingRequestDto bookingDto) {
        Item item = itemService.findItemById(bookingDto.getItemId());
        User user = userService.findUserById(userId);

        if (userId.equals(item.getOwner().getId())) {
            throw new RightsException();
        }
        if (!item.isAvailable()) {
            throw new ValidationException("item is unavailable");
        }

        Booking booking = mapToBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(WAITING);

        return toBookingResponseDto(bookingRepository.save(booking));
    }

    public BookingResponseDto approved(Long userId, Long bookingId, boolean approved) {
        try {
            userService.findUserById(userId);
        } catch (NotFoundException e) {
            throw new ValidationException("postman test need 400 error code");
        }
        Booking booking = findBookingById(bookingId);

        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new RightsException();
        }

        if (approved) {
            booking.setStatus(APPROVED);
        } else {
            booking.setStatus(REJECTED);
        }

        return toBookingResponseDto(bookingRepository.save(booking));
    }

    public List<BookingResponseDto> getAll(Long userId, String state) {
        userService.findUserById(userId);

        if (state.equals(ALL)) {
            return bookingRepository
                    .findAllByBookerId(userId)
                    .stream()
                    .map(BookingMapper::toBookingResponseDto)
                    .toList();
        }
        return bookingRepository
                .findAllByBookerIdAndStatus(userId, state)
                .stream()
                .map(BookingMapper::toBookingResponseDto)
                .toList();
    }

    public List<BookingResponseDto> getAllByItems(Long userId, String state) {
        userService.findUserById(userId);

        List<Long> items = itemRepository
                .findByOwnerId(userId)
                .stream()
                .map(Item::getId)
                .toList();

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        if (state.equals(ALL)) {
            return bookingRepository
                    .findAllByItemIdIn(items)
                    .stream()
                    .map(BookingMapper::toBookingResponseDto)
                    .toList();
        }
        return bookingRepository
                .findAllByItemIdInAndStatus(items, state)
                .stream()
                .map(BookingMapper::toBookingResponseDto)
                .toList();
    }

    public Booking findBookingById(Long id) {
        return bookingRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

}
