package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto save(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getItemById(Long userId, Long itemId);

    List<ItemDto> getAll(Long userId);

    void deleteItemById(Long userId, Long itemId);

    List<ItemDto> search(Long userId, String text);

}
