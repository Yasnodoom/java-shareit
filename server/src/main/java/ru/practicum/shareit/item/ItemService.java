package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.CommentRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RightsException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.item.dto.BookingItemDto.toBookingItemDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.mapToItemDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemOwnerDto;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public ItemDto create(ItemDto itemDto, Long userId) {
        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwner(userService.findUserById(userId));

        return mapToItemDto(itemRepository.save(item));
    }

    public ItemDto update(ItemDto item, Long itemId, Long userId) {
        Item oldItem = findItemById(itemId);
        userService.findUserById(userId);
        verificationRights(oldItem, userId);

        itemRepository.save(updateItemFields(oldItem, item));
        return mapToItemDto(oldItem);
    }

    public ItemOwnerDto get(Long itemId, Long userId) {
        User user = userService.findUserById(userId);
        Item item = findItemById(itemId);
        ItemOwnerDto itemOwnerDto = toItemOwnerDto(item);

        if (item.getOwner().equals(user)) {
            bookingRepository
                    .findLastBookingByItemId(itemId)
                    .ifPresent(el -> itemOwnerDto.setLastBooking(toBookingItemDto(el)));
            bookingRepository
                    .findNextBookingByItemId(itemId)
                    .ifPresent(el -> itemOwnerDto.setNextBooking(toBookingItemDto(el)));
        }
        itemOwnerDto.setComments(commentRepository.findAllByItemId(itemId));

        return itemOwnerDto;
    }

    public List<ItemDto> getAll(Long userId) {
        return itemRepository
                .findByOwnerId(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    public List<ItemDto> find(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository
                .findByNameIgnoreCase(text)
                .stream()
                .filter(Item::isAvailable)
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    public Comment addComment(Long userId, Long itemId, String text) {
        Item item = findItemById(itemId);
        User user = userService.findUserById(userId);

        if (!isUserBookingItem(userId, itemId)) {
            throw new RightsException();
        }

        bookingRepository.findAllByItemId(itemId)
                .stream()
                .filter(el -> el.getStatus().equals(Status.APPROVED))
                .filter(el -> el.getEnd().isAfter(LocalDateTime.now()))
                .findAny()
                .ifPresent(s -> {
                    throw new ValidationException("cant comment while booking active");
                });

        Comment comment = Comment
                .builder()
                .text(text)
                .item(item)
                .user(user)
                .authorName(user.getName())
                .build();

        return commentRepository.save(comment);
    }

    public Item findItemById(Long id) {
        return itemRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public List<Item> findItemsByRequestId(Long requestId) {
        return itemRepository.findByRequestId(requestId);
    }

    private Item updateItemFields(Item original, ItemDto update) {
        original.setName(update.getName());
        original.setDescription(update.getDescription());
        original.setAvailable(update.getAvailable());

        return original;
    }

    private void verificationRights(Item item, Long userId) {
        if (item.getOwner() != null && !item.getOwner().getId().equals(userId)) {
            throw new RightsException();
        }
    }

    private boolean isUserBookingItem(Long userId, Long itemId) {
        return bookingRepository
                .findAllByItemId(itemId)
                .stream()
                .anyMatch(el -> el.getBooker().getId().equals(userId));
    }

}
