package ru.practicum.shareit.booking.dto.validation;

import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingDateIsValid implements ConstraintValidator<StartEndTimeValid , BookItemRequestDto> {


    @Override
    public void initialize(StartEndTimeValid constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BookItemRequestDto bookItemRequestDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookItemRequestDto.getStart();
        LocalDateTime end = bookItemRequestDto.getEnd();

        if (start == null || end == null) {
            return false;
        }

        return start.isBefore(end);
    }
}
