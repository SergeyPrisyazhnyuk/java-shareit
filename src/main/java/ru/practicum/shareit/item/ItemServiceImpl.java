package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemDto save(Long userId, ItemDto itemDto) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userId);

        itemRepository.save(item);

        return ItemMapper.toItemDtoWithId(item);
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        Optional<Item> itemOpt = itemRepository.findById(itemId);

        if (itemOpt.isEmpty()) {
            throw new NotFoundException("Не найден айтем с id: " + itemId);
        }

        Item item = itemOpt.get();

        ItemDto newItemDto = ItemMapper.toItemDtoWithId(item);

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

        itemRepository.save(item);

        newItemDto.setId(itemId);

        return newItemDto;

    }

    @Override
    @Transactional
    public ItemDto getItemById(Long userId, Long itemId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        Optional<Item> itemOpt = itemRepository.findById(itemId);

        if (itemOpt.isEmpty()) {
            throw new NotFoundException("Не найден айтем с id: " + itemId);
        }

        Item item = itemOpt.get();

        return ItemMapper.toItemDtoWithId(item);
    }

    @Override
    @Transactional
    public List<ItemDto> getAll(Long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        List<Item> itemList = itemRepository.findAllByOwnerId(userId)
/*                .stream()
                .filter(u -> u.getOwner().equals(userId))
                .collect(Collectors.toList())*/;

        List<ItemDto> itemDtos = itemList
                .stream()
                .map(ItemMapper::toItemDtoWithId)
                .collect(Collectors.toList());

        return itemDtos;

    }

    @Override
    @Transactional
    public void deleteItemById(Long userId, Long itemId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        Optional<Item> itemOpt = itemRepository.findById(itemId);

        if (itemOpt.isEmpty()) {
            throw new NotFoundException("Не найден айтем с id: " + itemId);
        }

        Item item = itemOpt.get();

        if (!Objects.equals(userId, item.getOwner())) {
            throw new WrongUserException("Пользователь с id " + userId + " не является владельцем данной вещи и не может ее редактировать");
        }

        itemRepository.deleteById(itemId);

    }

    @Override
    @Transactional
    public List<ItemDto> search(Long userId, String text) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        if (text.isBlank()) {
            return Collections.emptyList();
        }


        List<Item> itemList = itemRepository.search(text);

        List<ItemDto> itemDtos = itemList
                .stream()
                .map(ItemMapper::toItemDtoWithId)
                .collect(Collectors.toList());

        return itemDtos;
    }
}
