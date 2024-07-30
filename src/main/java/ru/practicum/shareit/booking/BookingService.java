package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
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
import static ru.practicum.shareit.booking.BookingMapper.mapToBookingDto;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final UserService userService;

    public BookingDto get(Long userId, Long bookingId) {
        Booking booking = findBookingById(bookingId);

        if (booking.getBookerId().equals(userId)) {
            return BookingMapper.mapToBookingDto(booking);
        }

//        if (booking.getItem().getOwnerId().equals(userId)) {
//            return BookingMapper.mapToBookingDto(booking);
//        }
        throw new RightsException();
    }

    public BookingDto create(Long userId, BookingDto bookingDto) {
        validateBookingRequest(bookingDto, userId);

        Booking booking = mapToBooking(bookingDto);
        booking.setBookerId(userId);
        booking.setStatus(Status.WAITING.toString());

        return mapToBookingDto(bookingRepository.save(booking));
    }

    public BookingDto approved(Long userId, Long bookingId, boolean approved) {
        Booking booking = findBookingById(bookingId);

//        if (!userId.equals(booking.getItem().getOwnerId())) {
//            throw new RightsException();
//        }
        if (approved) {
            booking.setStatus(Status.APPROVED.toString());
        } else {
            booking.setStatus(Status.REJECTED.toString());
        }

        return mapToBookingDto(bookingRepository.save(booking));
    }

    public List<BookingDto> getAll(Long userId, String state) {
        return bookingRepository
                .findAllByBookerIdAndStatus(userId, state)
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    public List<BookingDto> getAllByItems(Long userId, String state) {
        List<Long> items = itemRepository
                .findByOwnerId(userId)
                .stream()
                .map(Item::getId)
                .toList();

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        return bookingRepository
                .findAllByItemIdInAndStatus(items, state)
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    public Booking findBookingById(Long id) {
        return bookingRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    private void validateBookingRequest(BookingDto booking, Long userId) {
        Item item = itemService.findItemById(booking.getItemId());
        userService.findUserById(userId);

        if (userId.equals(item.getOwner().getId())) {
            throw new RightsException();
        }
        if (!item.isAvailable()) {
            throw new ValidationException("item is unavailable");
        }
    }

}
