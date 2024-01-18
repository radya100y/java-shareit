package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BookingWithoutDates {

    private long id;

    private BookingStatus status;

    private Item item;

    private User booker;
}
