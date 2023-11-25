package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Accessors(chain = true)
public class ItemDto {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private Long owner;

    private Long request;

    private BookingDtoReturn lastBooking;

    private BookingDtoReturn nextBooking;

    public ItemDto(Long id, String name, String description, Boolean available, Long owner, Long request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }

    public ItemDto(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
