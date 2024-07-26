package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemOwnerDto {
    private String name;
    private String description;
    private Boolean available;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long ownerId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long requestId;
    private String lastBookingDate = "даты последнего бронирования";
    private String nextBookingDate = "даты ближайщего следующего бронирования";
}

