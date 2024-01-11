package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data @AllArgsConstructor @RequiredArgsConstructor
public class CommentRequest {

    @NotNull
    private Long itemId;

    @NotNull
    private long userId;

    @NotBlank
    @Size(max = 1024)
    private String text;
}
