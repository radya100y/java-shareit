package ru.practicum.shareit.error;

public class AlreadyExistException extends RuntimeException {

    private String message;

    public AlreadyExistException(String msg) {
        super(msg);
    }
}
