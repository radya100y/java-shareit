package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.dto.BookingSmall;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@SuperBuilder
@RequiredArgsConstructor
public class ItemDto {

    private long id;

    @Size(max = 255)
    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 512)
    private String description;

    @NotNull
    private Boolean available;

    private long owner;

    private BookingSmall lastBooking;

    private BookingSmall nextBooking;

    private List<CommentResponse> comments;

    private Long requestId;
}
