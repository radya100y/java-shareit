package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public Booking save(@Valid @RequestBody BookingDto booking, @RequestHeader("X-Sharer-User-Id") long userId) {
        booking.setUserId(userId);
        booking.setStatus(BookingStatus.WAITING);
        return bookingService.save(booking);
    }
}
