package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public static BookingDto mapToBookingDto(Booking booking) {
        return BookingDto
                .builder()
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .bookerId(booking.getBookerId())
                .status(booking.getStatus())
                .build();
    }

}
