package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class CommentResponse {

    private long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
