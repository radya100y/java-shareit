package ru.practicum.shareit.error;

import lombok.Getter;

@Getter
public class AccessException extends RuntimeException {

    private String message;

    public AccessException(String message) {
        super(message);
        this.message = message;
    }
}
