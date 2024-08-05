package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentText {
    @NotNull
    private String text;
}
