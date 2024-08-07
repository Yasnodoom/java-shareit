package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import static ru.practicum.shareit.booking.Status.ALL;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                      @PathVariable Long bookingId) {
        return bookingClient.get(userId, bookingId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                         @RequestBody final BookingRequestDto bookingDto) {
        return bookingClient.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approved(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                           @PathVariable Long bookingId,
                                           @RequestParam boolean approved) {
        return bookingClient.approved(userId, bookingId, approved);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                         @RequestParam(required = false, defaultValue = ALL) String state) {
        return bookingClient.getAll(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByItems(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                @RequestParam(required = false, defaultValue = ALL) String state) {
        return bookingClient.getAllByItems(userId, state);
    }

}
