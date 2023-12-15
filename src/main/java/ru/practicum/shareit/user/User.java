package ru.practicum.shareit.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.Entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class User extends Entity {

    private long id;

    private long name;

    @Email
    @NotBlank
    private long email;
}
