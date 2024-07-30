package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public static BookingDto mapToBookingDto(Booking booking) {
        return BookingDto
                .builder()
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItemId())
                .bookerId(booking.getBookerId())
                .status(booking.getStatus())
                .build();
    }

    public static Booking mapToBooking(BookingDto bookingDto) {
        return  Booking
                .builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .itemId(bookingDto.getItemId())
                .bookerId(bookingDto.getBookerId())
                .build();
    }
}
