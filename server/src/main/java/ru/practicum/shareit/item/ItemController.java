package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentText;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@RequestHeader(X_SHARER_USER_ID) Long userId,
                          @Valid @RequestBody final ItemDto item) {
        return itemService.create(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(X_SHARER_USER_ID) Long userId,
                          @PathVariable Long itemId,
                          @Valid @RequestBody final ItemDto item) {
        return itemService.update(item, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemOwnerDto get(@RequestHeader(X_SHARER_USER_ID) Long userId,
                            @PathVariable Long itemId) {
        return itemService.get(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> find(@RequestParam String text) {
        return itemService.find(text);
    }

    @PostMapping("/{itemId}/comment")
    public Comment addComment(@RequestHeader(X_SHARER_USER_ID) Long userId,
                              @PathVariable Long itemId,
                              @Valid @RequestBody final CommentText text) {
        return itemService.addComment(userId, itemId, text.getText());
    }

}