package ru.practicum.shareit.user;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {

    private long id;

    private String name;

    @Email
    @NotBlank
    private String email;
}
