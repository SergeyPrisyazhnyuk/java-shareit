package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

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

    private BookItemRequestDto lastBooking;

    private BookItemRequestDto nextBooking;

    private List<CommentDtoReturn> comments;

    public ItemBookingDto(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
