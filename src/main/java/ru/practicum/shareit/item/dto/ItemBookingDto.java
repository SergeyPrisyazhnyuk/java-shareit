package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemBookingDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingDtoReturn lastBooking;

    private BookingDtoReturn nextBooking;

    private List<CommentDtoReturn> comments;

    public ItemBookingDto(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
