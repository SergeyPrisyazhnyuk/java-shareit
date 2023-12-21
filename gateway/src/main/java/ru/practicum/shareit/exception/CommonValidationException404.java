package ru.practicum.shareit.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommonValidationException404 extends RuntimeException {
    public CommonValidationException404(String message) {
        super(message);
    }
}