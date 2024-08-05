package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

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
    private List<Comment> comments;
}

