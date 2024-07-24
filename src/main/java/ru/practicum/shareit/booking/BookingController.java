package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public BookingDto get(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                   @PathVariable Long bookingId) {
        return bookingService.get(userId, bookingId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader(X_SHARER_USER_ID) Long userId,
                             @RequestBody final BookingDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approved(@RequestHeader(X_SHARER_USER_ID) Long userId,
                               @PathVariable Long bookingId,
                               @RequestParam Boolean approved) {
        return bookingService.approved(userId, bookingId, approved);
    }

    @GetMapping
    public List<BookingDto> getAll(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                   @RequestParam String state) {
        return bookingService.getAll(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByItems(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                          @RequestParam String state) {
        return bookingService.getAllByItems(userId, state);
    }

}
