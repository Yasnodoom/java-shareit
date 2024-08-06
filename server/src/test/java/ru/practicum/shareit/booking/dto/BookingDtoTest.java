package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {
    private final JacksonTester<BookingRequestDto> json;

    @Test
    void testSerialize() throws Exception {
        BookingRequestDto bookingRequestDto = BookingRequestDto
                .builder()
                .itemId(1L)
                .bookerId(2L)
                .status(Status.APPROVED)
                .end(LocalDateTime.now())
                .build();

        JsonContent<BookingRequestDto> result = json.write(bookingRequestDto);

        assertThat(result)
                .hasJsonPath("$.itemId")
                .hasJsonPath("$.bookerId")
                .hasJsonPath("$.status")
                .hasJsonPath("$.end");

        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .satisfies(itemId -> assertThat(itemId.longValue()).isEqualTo(bookingRequestDto.getItemId()));
        assertThat(result).extractingJsonPathNumberValue("$.bookerId")
                .satisfies(bookerId -> assertThat(bookerId.longValue()).isEqualTo(bookingRequestDto.getBookerId()));
        assertThat(result).extractingJsonPathStringValue("$.status")
                .satisfies(status -> assertThat(status).isEqualTo(bookingRequestDto.getStatus().name()));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .satisfies(end -> assertThat(end).isNotNull());
    }
}
