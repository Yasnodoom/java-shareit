package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto create(@RequestHeader(X_SHARER_USER_ID) Long requesterId,
                                 @RequestBody final ItemRequestDto request) {
        return itemRequestService.create(request, requesterId);
    }

    @GetMapping
    public List<ItemRequestDto> findPersonal(@RequestHeader(X_SHARER_USER_ID) Long requesterId) {
        return itemRequestService.findPersonal(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll(@RequestParam(required = false, defaultValue = "0") Integer from,
                                        @RequestParam(required = false, defaultValue = "10") Integer size) {
        return itemRequestService.findAll(from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getOne(@PathVariable Long requestId) {
        return itemRequestService.getOne(requestId);
    }
}

