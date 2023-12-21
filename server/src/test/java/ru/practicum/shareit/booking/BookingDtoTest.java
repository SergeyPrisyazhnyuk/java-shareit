package ru.practicum.shareit.booking;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

@JsonTest
public class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;


    LocalDateTime start = LocalDateTime.now().plusMinutes(2);

    LocalDateTime end = LocalDateTime.now().plusMinutes(10);

    private BookingDto bookingDto = null;

    @BeforeEach
    public void before() {
        bookingDto = new BookingDto(0L, start, end);
    }

    @Test
    @SneakyThrows
    public void testStartTime() {

        JsonContent<BookingDto> jsonContent = this.json.write(bookingDto);

        jsonContent.assertThat().hasJsonPathStringValue("@.start", start);

    }

    @Test
    @SneakyThrows
    public void testEndTime() {

        JsonContent<BookingDto> jsonContent = this.json.write(bookingDto);

        jsonContent.assertThat().hasJsonPathStringValue("@.end", end);

    }

}
