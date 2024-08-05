package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {
    public static ItemRequest toRequest(ItemRequestDto dto) {
        return ItemRequest
                .builder()
                .desc(dto.getDescription())
                .build();
    }

    public static ItemRequestDto toDto(ItemRequest request) {
        return ItemRequestDto
                .builder()
                .id(request.getId())
                .description(request.getDesc())
                .created(request.getCreated())
                .requesterId(request.getRequester().getId())
                .build();
    }
}
