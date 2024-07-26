package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RightsException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.item.mapper.ItemMapper.mapToItemDto;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public ItemDto create(ItemDto itemDto, Long userId) {
        userService.findUserById(userId);
        validate(itemDto);

        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwnerId(userId);

        return mapToItemDto(itemRepository.save(item));
    }

    public ItemDto update(ItemDto item, Long itemId, Long userId) {
        Item oldItem = findItemById(itemId);
        userService.findUserById(userId);
        verificationRights(oldItem, userId);

        itemRepository.save(updateItemFields(oldItem, item));
        return mapToItemDto(oldItem);
    }

    public ItemDto get(Long itemId) {
        return mapToItemDto(findItemById(itemId));
    }

    public List<ItemDto> getAll(Long userId) {
        return itemRepository
                .findByUserId(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    public List<ItemDto> find(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository
                .findByDescription(text)
                .stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    public void addComment(Long userId, Long itemId, String text) {
        if (!isUserBookingItem(findItemById(itemId))) {
            throw new RightsException();
        }
        Comment comment = Comment
                .builder()
                .text(text)
                .itemId(itemId)
                .userId(userId)
                .build();
        commentRepository.save(comment);
    }

    private Item updateItemFields(Item original, ItemDto update) {
        if (update.getName() != null && !update.getName().isEmpty()) {
            original.setName(update.getName());
        }
        if (update.getDescription() != null && !update.getDescription().isEmpty()) {
            original.setDescription(update.getDescription());
        }
        if (update.getAvailable() != null) {
            original.setAvailable(update.getAvailable());
        }
        return original;
    }

    private Item findItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    private void validate(ItemDto item) {
        if (item.getAvailable() == null)
            throw new ValidationException("available is empty");
        if (item.getName() == null || item.getName().isEmpty() || item.getName().isBlank())
            throw new ValidationException("name is empty");
        if (item.getDescription() == null || item.getDescription().isEmpty() || item.getDescription().isBlank())
            throw new ValidationException("description is empty");
    }

    private void verificationRights(Item item, Long userId) {
        if (item.getOwnerId() != null && !item.getOwnerId().equals(userId)) {
            throw new RightsException();
        }
    }

    private boolean isUserBookingItem(Item item) {
        return !bookingRepository.findAllByItemsAndState(List.of(item), "ALL").isEmpty();
    }

}
