package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank
    @NotEmpty
    private String description;
    private Long requesterId;
    private LocalDateTime created;
    private List<Item> items;
}
