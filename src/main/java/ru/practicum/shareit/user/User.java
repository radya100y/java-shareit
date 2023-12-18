package ru.practicum.shareit.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@SuperBuilder
@RequiredArgsConstructor
public class User {

    private long id;

    private String name;

    @Email
    @NotBlank
    private String email;
}
