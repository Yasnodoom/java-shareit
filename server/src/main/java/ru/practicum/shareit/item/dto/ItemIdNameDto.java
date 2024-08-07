package ru.practicum.shareit.item.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ItemIdNameDto {
    private Long id;
    private String name;
}

