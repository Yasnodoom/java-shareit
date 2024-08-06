package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ItemDto {
    private Long id;

    @NotEmpty
    @NotBlank
    private String name;

    @NotEmpty
    @NotBlank
    private String description;

    @NonNull
    private Boolean available;

    private Long ownerId;
    private Long requestId;
}

