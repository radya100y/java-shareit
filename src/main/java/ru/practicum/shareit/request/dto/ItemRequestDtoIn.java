package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

//@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class ItemRequestDtoIn {

    private Long userId;

    @NotBlank
    @Size(max = 1024)
    private String description;

    private LocalDateTime created = LocalDateTime.now();

    public ItemRequestDtoIn(Long userId, String description) {
        this.userId = userId;
        this.description = description;
    }
}
