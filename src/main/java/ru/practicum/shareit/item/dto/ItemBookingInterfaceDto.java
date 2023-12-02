package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemBookingInterfaceDto {
    private Long id;

    private String name;

    public ItemBookingInterfaceDto(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
    }
}