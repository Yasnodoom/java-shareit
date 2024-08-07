package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.dto.ItemIdNameDto;
import ru.practicum.shareit.user.model.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    private BookingResponseDto booking;
    private BookingRequestDto bookingRequestDto;
    private final List<BookingResponseDto> bookings = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void initData() {
        booking = BookingResponseDto.builder()
                .id(1L)
                .booker(new UserDto())
                .item(new ItemIdNameDto())
                .status(Status.APPROVED)
                .build();
        bookings.add(booking);
        bookingRequestDto = BookingRequestDto.builder()
                .end(LocalDateTime.now())
                .start(LocalDateTime.now())
                .status(Status.APPROVED)
                .build();
    }

    @AfterEach
    void clearData() {
        bookings.clear();
    }

    @Test
    void findAllEmpty() throws Exception {
        when(bookingService.getAll(anyLong(), anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings").header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(bookingService, times(1)).getAll(anyLong(), anyString());
    }

    @Test
    void findAll() throws Exception {
        when(bookingService.getAll(anyLong(), anyString())).thenReturn(bookings);

        mockMvc.perform(get("/bookings").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.[0].status", is(Status.APPROVED.name())));
        verify(bookingService, times(1)).getAll(anyLong(), anyString());
    }

    @Test
    void create() throws Exception {
        when(bookingService.create(anyLong(), any())).thenReturn(booking);
        bookingRequestDto.setStart(null);
        bookingRequestDto.setEnd(null);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.end", is(Matchers.nullValue())));
        verify(bookingService, times(1)).create(anyLong(), any());
    }

    @Test
    void getOne() throws Exception {
        when(bookingService.get(anyLong(), anyLong())).thenReturn(booking);

        mockMvc.perform(get("/bookings/1").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(Status.APPROVED.name())));
        verify(bookingService, times(1)).get(anyLong(), anyLong());
    }

    @Test
    void approved() throws Exception {
        when(bookingService.approved(any(), anyLong(), anyBoolean())).thenReturn(booking);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(Status.APPROVED.name())));
        verify(bookingService, times(1)).approved(any(), anyLong(), anyBoolean());
    }
}
