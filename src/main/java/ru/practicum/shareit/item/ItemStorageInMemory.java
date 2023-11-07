package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorageInMemory;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemStorageInMemory implements ItemStorage {

    private final UserStorageInMemory userStorageInMemory;

    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 1L;

    public Long setIdReturn() {
        return id++;
    }

    @Override
    public ItemDto save(Long userId, ItemDto itemDto) {

        if (userStorageInMemory.getUserById(userId) == null) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        Item item = ItemMapper.toItem(itemDto);
        Long id = setIdReturn();

        item.setId(id);
        item.setOwner(userId);
        itemDto.setId(id);
        itemDto.setOwner(userId);
        items.put(id, item);
        return itemDto;

    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {

        if (userStorageInMemory.getUserById(userId) == null) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        if (items.get(itemId) == null) {
            throw new NotFoundException("Не найден айтем с id: " + itemId);
        }

        Item item = items.get(itemId);
        ItemDto newItemDto = ItemMapper.toItemDtoWithId(item);

        System.out.println(newItemDto);

        if (!Objects.equals(userId, item.getOwner())) {
            throw new WrongUserException("Пользователь с id " + userId + " не является владельцем данной вещи и не может ее редактировать");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
            newItemDto.setName(itemDto.getName());
        } else {
            newItemDto.setName(item.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
            newItemDto.setDescription(itemDto.getDescription());
        } else {
            newItemDto.setDescription(item.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
            newItemDto.setAvailable(itemDto.getAvailable());
        } else {
            newItemDto.setAvailable(item.getAvailable());
        }

        items.put(itemId, item);
        newItemDto.setId(itemId);

        System.out.println(newItemDto);

        return newItemDto;
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {

        if (userStorageInMemory.getUserById(userId) == null) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        if (items.get(itemId) == null) {
            throw new NotFoundException("Не найден айтем с id: " + itemId);
        }

        Item item = items.get(itemId);
        ItemDto itemDto = ItemMapper.toItemDtoWithId(item);

        return itemDto;
    }

    @Override
    public List<ItemDto> getAll(Long userId) {

        if (userStorageInMemory.getUserById(userId) == null) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        userStorageInMemory.getUserById(userId);

        List<Item> itemList = items.values()
                                        .stream()
                                        .filter(u -> u.getOwner().equals(userId))
                                        .collect(Collectors.toList());

        List<ItemDto> itemDtos = itemList
                                        .stream()
                                        .map(ItemMapper::toItemDtoWithId)
                                        .collect(Collectors.toList());

        return itemDtos;
    }

    @Override
    public void deleteItemById(Long userId, Long itemId) {
        if (userStorageInMemory.getUserById(userId) == null) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        if (items.get(itemId) == null) {
            throw new NotFoundException("Не найден айтем с id: " + itemId);
        }

        if (!Objects.equals(userId, items.get(itemId).getOwner())) {
            throw new WrongUserException("Пользователь с id " + userId + " не является владельцем данной вещи и не может ее редактировать");
        }

        items.remove(itemId);
    }


    @Override
    public List<ItemDto> search(Long userId, String text) {

        List<ItemDto> itemDtos = new ArrayList<>();

        if (!text.isEmpty()) {
            List<Item> itemList = items.values()
                    .stream()
                    .filter(Item::getAvailable)
                    .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase())
                            || i.getDescription().toLowerCase().contains(text.toLowerCase())
                    )
                    .collect(Collectors.toList());

           itemDtos = itemList
                    .stream()
                    .map(ItemMapper::toItemDtoWithId)
                    .collect(Collectors.toList());
        }
        return itemDtos;
    }
}
