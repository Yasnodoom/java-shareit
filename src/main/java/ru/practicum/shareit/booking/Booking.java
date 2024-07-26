package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Data
@Builder
@Entity(name = "Bookings")
public class Booking {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "booking_id")
    private Long id;

    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private Long bookerId;
    private Status status;
}
