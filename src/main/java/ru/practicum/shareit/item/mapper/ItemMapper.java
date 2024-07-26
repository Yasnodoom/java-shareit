package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    private static CommentRepository commentRepository;

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .requestId(item.getRequestId())
                .comments(commentRepository.findAllByItemId(item.getId()))
                .build();
    }

    public static Item mapToItem(ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .ownerId(itemDto.getOwnerId())
                .requestId(itemDto.getRequestId())
                .build();
    }
}
