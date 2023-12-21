package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ItemDto {
    private Long id;


    private String name;


    private String description;


    private Boolean available;

    private Long owner;

    private Long requestId;

    private BookingDtoReturn lastBooking;

    private BookingDtoReturn nextBooking;

    private List<CommentDtoReturn> comments;

    public ItemDto(Long id, String name, String description, Boolean available, Long owner, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.requestId = requestId;
    }

    public ItemDto(String name, String description, Boolean available, Long owner, Long requestId) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.requestId = requestId;
    }

}
