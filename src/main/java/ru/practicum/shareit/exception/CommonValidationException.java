package ru.practicum.shareit.exception;

public class CommonValidationException extends RuntimeException {
    public CommonValidationException(String message) {
        super(message);
    }
}