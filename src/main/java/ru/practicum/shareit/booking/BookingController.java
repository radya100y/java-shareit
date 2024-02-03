package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.error.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public Booking save(@Valid @RequestBody BookingDto booking, @RequestHeader("X-Sharer-User-Id") long userId) {
        booking.setUserId(userId);
        booking.setStatus(BookingStatus.WAITING);
        return bookingService.save(booking);
    }

    @PatchMapping("/{id}")
    public Booking update(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable("id") long bookingId,
                                      @RequestParam String approved) {
        BookingStatus bookingStatus;
        if (approved.equals("true")) bookingStatus = BookingStatus.APPROVED;
        else if (approved.equals("false")) bookingStatus = BookingStatus.REJECTED;
        else throw new NotFoundException("Ошибка статуса бронирования");
        return bookingService.update(bookingId, userId, bookingStatus);
    }

    @GetMapping("/{id}")
    public Booking getCertain(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable("id") long bookingId) {
        return bookingService.getCertain(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getBookingsForBooker(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(defaultValue = "ALL") BookingStatusParam state,
                                              @RequestParam(defaultValue = "0") @Min(0) int from,
                                              @RequestParam(defaultValue = "20") @Min(1) int size) {
        Pageable reqPage = PageRequest.of(from / size, size, Sort.by("id").descending());
        return bookingService.getBookingForBooker(userId, state, reqPage);
    }

    @GetMapping("/owner")
    public List<Booking> getBookingsForOwner(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "ALL") BookingStatusParam state,
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "20") @Min(1) int size) {
        Pageable reqPage = PageRequest.of(from / size, size, Sort.by("id").descending());
        return bookingService.getBookingForOwner(userId, state, reqPage);
    }
}
