package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;

    @Override
    public ItemDto save(Long userId, ItemDto itemDto) {
        return itemStorage.save(userId, itemDto);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        return itemStorage.update(userId, itemId, itemDto);
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        return itemStorage.getItemById(userId, itemId);
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        return itemStorage.getAll(userId);
    }

    @Override
    public void deleteItemById(Long userId, Long itemId) {
        itemStorage.deleteItemById(userId, itemId);
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        return itemStorage.search(userId, text);
    }
}
