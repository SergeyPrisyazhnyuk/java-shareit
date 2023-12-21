package ru.practicum.shareit.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CommonValidationException400 extends RuntimeException {
    public CommonValidationException400(String message) {
        super(message);
    }
}