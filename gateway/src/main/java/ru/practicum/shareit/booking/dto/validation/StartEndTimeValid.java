package ru.practicum.shareit.booking.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BookingDateIsValid.class)
public @interface StartEndTimeValid {

    String message() default "Start can't be after end";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
