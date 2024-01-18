package ru.practicum.shareit.item.dto;


import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingSmall;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

@UtilityClass
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .build();
    }

    public static Item toItem(ItemDto item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .build();
    }

    public static BookingSmall toBookingSmall(Booking booking) {
        return new BookingSmall(
                booking.getId(),
                booking.getBooker().getId()
        );
    }
}
