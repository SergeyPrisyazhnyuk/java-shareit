package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest {

    @Test
    void handleWrongStateException() {
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse errorResponse = errorHandler.handleWrongStateException(new WrongStateException("WrongStateException"));

        assertNotNull(errorResponse);
    }
}