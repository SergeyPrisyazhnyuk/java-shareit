package ru.practicum.shareit.request;

import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoReturn;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemRequestMapper {

/*
    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder().description(itemRequestDto.getDescription())
                .build();
    }
*/

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(
                itemRequestDto.getDescription()
        );
    }

    public static ItemRequestDtoReturn toItemRequestDtoReturn(ItemRequest itemRequest) {

        List<ItemDto> itemDtoList = new ArrayList<>();

        if (!Objects.isNull(itemRequest.getItems())) {
            itemDtoList = itemRequest.getItems()
                    .stream()
                    .map(ItemMapper :: toItemDtoWithId)
                    .collect(Collectors.toList());
        }

        return new ItemRequestDtoReturn(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemDtoList

        );
    }


}
