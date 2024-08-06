package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequestDto {
    private Long id;
    @NotBlank
    @NotEmpty
    private String description;
    private Long requesterId;
    private LocalDateTime created;
}
