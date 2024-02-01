package ru.practicum.shareit.error;

import lombok.Getter;

@Getter
public class AccessException extends RuntimeException {

    private final String message;

    public AccessException(String message) {
        super(message);
        this.message = message;
    }
}
