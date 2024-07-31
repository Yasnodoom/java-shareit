package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        return BookingResponseDto
                .builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.toIdNameItem(booking.getItem()))
                .booker(UserMapper.mapToUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public static Booking mapToBooking(BookingRequestDto bookingDto) {
        return Booking
                .builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();
    }
}
