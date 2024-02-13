package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserOut {
    private Long id;
    private String name;
    private String email;
}
