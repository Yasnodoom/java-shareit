package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class BookingItemDto {
    private final Long bookingId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Long bookerId;

    public static BookingItemDto toBookingItemDto(BookingDatesDto bookingDatesDto) {
        return BookingItemDto
                .builder()
                .bookingId(bookingDatesDto.getBookingId())
                .startDate(bookingDatesDto.getStartDate())
                .endDate(bookingDatesDto.getEndDate())
                .bookerId(bookingDatesDto.getBookerId())
                .build();
    }
}
