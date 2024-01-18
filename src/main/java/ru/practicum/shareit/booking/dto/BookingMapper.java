package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@UtilityClass
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .userId(booking.getBooker().getId())
                .build();
    }

    public static Booking toBooking(BookingDto booking, Item item, User booker) {
        return Booking.builder()
                .id(booking.getId())
                .item(item)
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(booker)
                .build();
    }

    public static BookingWithoutDates toBookingWithoutDates(Booking booking) {
        return BookingWithoutDates.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .build();
    }
}
