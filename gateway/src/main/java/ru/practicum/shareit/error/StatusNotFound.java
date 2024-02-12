package ru.practicum.shareit.error;

import lombok.Getter;

@Getter
public class StatusNotFound extends RuntimeException {

    private final String message;

    public StatusNotFound(String message) {
        super(message);
        this.message = message;
    }
}
