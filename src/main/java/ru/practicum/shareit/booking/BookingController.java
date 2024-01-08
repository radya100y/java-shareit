package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public BookingDto save(@Valid @RequestBody BookingDto booking, @RequestHeader("X-Sharer-User-Id") long userId) {
        booking.setUserId(userId);
        return bookingService.save(booking);
    }
}
