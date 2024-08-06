package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentText {
    @NotNull
    private String text;
}
