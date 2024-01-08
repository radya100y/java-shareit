package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;

@UtilityClass
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getItemId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getUserId()
        );
    }

    public static Booking toBooking(BookingDto booking) {
        return new Booking(
                booking.getId(),
                booking.getItemId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getUserId()
        );
    }
}
