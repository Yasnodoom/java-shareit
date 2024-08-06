package ru.practicum.shareit.bookingold;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.bookingold.dto.BookingRequestDto;
import ru.practicum.shareit.bookingold.dto.BookingResponseDto;

import java.util.List;

import static ru.practicum.shareit.bookingold.Status.ALL;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public BookingResponseDto get(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                  @PathVariable Long bookingId) {
        return bookingService.get(userId, bookingId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto create(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                     @RequestBody final BookingRequestDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approved(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                       @PathVariable Long bookingId,
                                       @RequestParam boolean approved) {
        return bookingService.approved(userId, bookingId, approved);
    }

    @GetMapping
    public List<BookingResponseDto> getAll(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                           @RequestParam(required = false, defaultValue = ALL) String state) {
        return bookingService.getAll(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllByItems(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                  @RequestParam(required = false, defaultValue = ALL) String state) {
        return bookingService.getAllByItems(userId, state);
    }

}
