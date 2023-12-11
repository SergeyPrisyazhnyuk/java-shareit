package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemBookingInterfaceDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoReturn {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingStatus status;

    private ItemBookingInterfaceDto item;

    private UserDto booker;

    private Long bookerId;

    public Long getItemId() {
        return item.getId();
    }

    public BookingDtoReturn(Long id, Long bookerId) {
        this.id = id;
        this.bookerId = bookerId;

    }

}
