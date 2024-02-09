package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class ItemUpd {

    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 512)
    private String description;

    private Boolean available;
}