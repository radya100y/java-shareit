package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data @RequiredArgsConstructor @AllArgsConstructor
@Entity @Table(name = "bookings", schema = "public")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "item_id", nullable = false)
    private long itemId;

    @Column(name = "pb", nullable = false)
    private LocalDateTime start;

    @Column(name = "pe", nullable = false)
    private LocalDateTime end;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "user_id", nullable = false)
    private long userId;
}
