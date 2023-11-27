package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemBookingDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingDtoReturn lastBooking;

    private BookingDtoReturn nextBooking;

/*    public ItemBookingDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }*/

    public ItemBookingDto(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
