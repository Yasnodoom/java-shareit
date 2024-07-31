package ru.practicum.shareit.item.dto;

import java.time.LocalDateTime;

public interface BookingDatesDto {
    Long getBookingId();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    Long getBookerId();
}
