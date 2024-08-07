package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestClient requestClient;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) Long requesterId,
                                         @RequestBody @Valid final ItemRequestDto request) {
        return requestClient.create(requesterId, request);
    }

    @GetMapping
    public ResponseEntity<Object> findPersonal(@RequestHeader(X_SHARER_USER_ID) Long requesterId) {
        return requestClient.findPersonal(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestHeader(X_SHARER_USER_ID) Long requesterId,
                                          @RequestParam(required = false, defaultValue = "0") Integer from,
                                          @RequestParam(required = false, defaultValue = "10") Integer size) {
        return requestClient.findAll(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getOne(@PathVariable Long requestId) {
        return requestClient.getOne(requestId);
    }
}

