package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@SuperBuilder
public class UserDto {

    private long id;

    private String name;

    @Email
    @NotBlank
    private String email;
}
