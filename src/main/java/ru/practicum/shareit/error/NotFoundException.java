package ru.practicum.shareit.error;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private String message;

    public NotFoundException(String msg) {
        super(msg);
        this.message = msg;
    }
}
