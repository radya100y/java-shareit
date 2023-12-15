package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.Entity;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ItemDto extends Entity {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private long bookingQty;
}
