package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RightsException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

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

    public BookingDto create(@RequestHeader(X_SHARER_USER_ID) Long userId,
                             @RequestBody final BookingDto bookingDto) {
        return bookingRepository.create(userId, bookingDto);
    }

    public BookingDto approved(@RequestHeader(X_SHARER_USER_ID) Long userId,
                               @PathVariable Long bookingId,
                               @RequestParam Boolean approved) {
        return bookingRepository.approved(userId, bookingId, approved);
    }

    public List<BookingDto> getAll(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                   @RequestParam String state) {
        return bookingRepository.getAll(userId, state);
    }

    public List<BookingDto> getAllByItems(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                   @RequestParam String state) {
        return bookingRepository.getAllByItems(userId, state);
    }

    public Booking findBookingById(Long id) {
        return bookingRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }


}
