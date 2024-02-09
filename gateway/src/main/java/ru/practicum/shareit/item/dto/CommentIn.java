package ru.practicum.shareit.item.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CommentIn {

    @NotBlank
    @Size(max = 1024)
    private String text;
}
