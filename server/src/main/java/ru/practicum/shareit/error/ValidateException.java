package ru.practicum.shareit.error;

import lombok.Getter;

@Getter
public class ValidateException extends RuntimeException {

    private final String message;

    public ValidateException(String message) {
        super(message);
        this.message = message;
    }
}
