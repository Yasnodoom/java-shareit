package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                         @RequestBody @Valid final ItemDto item) {
        return itemClient.create(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody @Valid final ItemDto item) {
        return itemClient.update(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                      @PathVariable Long itemId) {
        return itemClient.get(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemClient.getAll(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> find(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                       @RequestParam String text) {
        return itemClient.find(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody final CommentText text) {
        return itemClient.addComment(userId, itemId, text.getText());
    }
}