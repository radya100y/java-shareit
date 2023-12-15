package ru.practicum.shareit;

public class NotFoundException extends RuntimeException {

    private String message;

    public NotFoundException(String msg) {
        super(msg);
    }
}
