package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RightsException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.booking.BookingMapper.mapToBooking;
import static ru.practicum.shareit.booking.BookingMapper.mapToBookingDto;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    public BookingDto get(Long userId, Long bookingId) {
        Booking booking = findBookingById(bookingId);

        if (booking.getBookerId().equals(userId)) {
            return BookingMapper.mapToBookingDto(booking);
        }

        if (booking.getItem().getOwnerId().equals(userId)) {
            return BookingMapper.mapToBookingDto(booking);
        }
        throw new RightsException();
    }

    public BookingDto create(Long userId, BookingDto bookingDto) {
        Booking booking = mapToBooking(bookingDto);
        booking.setBookerId(userId);
        booking.setStatus(Status.WAITING);

        return mapToBookingDto(bookingRepository.save(booking));
    }

    public BookingDto approved(Long userId, Long bookingId, boolean approved) {
        Booking booking = findBookingById(bookingId);

        if (!userId.equals(booking.getItem().getOwnerId())) {
            throw new RightsException();
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return mapToBookingDto(bookingRepository.save(booking));
    }

    public List<BookingDto> getAll(Long userId, String state) {
        return bookingRepository
                .findAllByUserIdAndState(userId, state)
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    public List<BookingDto> getAllByItems(Long userId,String state) {
        List<Item> items = itemRepository.findByUserId(userId);

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        return bookingRepository
                .findAllByItemsAndState(items, state)
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    public Booking findBookingById(Long id) {
        return bookingRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

}
