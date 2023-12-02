package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.exception.CommonValidationException400;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoReturn;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

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

        Optional<Item> itemO = itemRepository.findById(itemId);

        if (itemO.isEmpty()) {
            throw new NotFoundException("Не найден айтем с id: " + itemId);
        }

        Item item = itemO.get();
        ItemDto itemDto = ItemMapper.toItemDtoWithId(item);

        itemDto.setComments(getAllCommentsByItemId(itemId));

        if (!userId.equals(itemDto.getOwner())) {
            return itemDto;
        }

        itemDto.setLastBooking(findItemLastBookingById(itemId, LocalDateTime.now()));
        itemDto.setNextBooking(findItemNextBookingById(itemId, LocalDateTime.now()));

        return itemDto;
    }

    @Override
    @Transactional
    public List<ItemBookingDto> getAll(Long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        List<Item> itemList = itemRepository.findAllByOwnerId(userId)
                .stream()
                .sorted(Comparator.comparing(Item::getId))
                .collect(Collectors.toList());

        List<Long> itemIdList = itemList.stream()
                .map(Item::getId)
                .collect(toList());

        Map<Long, List<CommentDtoReturn>> comments = commentRepository.findAllByItemIdIn(itemIdList).stream()
                .map(CommentMapper::toCommentDtoReturn)
                .collect(groupingBy(CommentDtoReturn::getItemId, toList()));

        Map<Long, List<BookingDtoReturn>> bookingDtoReturnMap = bookingRepository.findAllByItemInAndStatusOrderByStartAsc(itemList, BookingStatus.APPROVED)
                .stream()
                .map(BookingMapper::bookingDtoReturnFromInterface)
                .collect(groupingBy(BookingDtoReturn::getItemId, toList()));

        List<ItemBookingDto> itemDtos = new ArrayList<>();

        if (bookingDtoReturnMap.isEmpty()) {
            itemDtos = itemList
                    .stream()
                    .map(ItemMapper::toItemBookingDto)
                    .collect(toList());
        } else {

            itemDtos = itemList
                    .stream()
                    .map(item -> ItemMapper.toItemBookingDto(
                            item,
                            findItemLastBooking(bookingDtoReturnMap.get(item.getId()), LocalDateTime.now()),
                            findItemNextBooking(bookingDtoReturnMap.get(item.getId()), LocalDateTime.now()),
                            comments.get(item.getId())

                    ))
                    .collect(toList());
        }

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

        return itemList
                .stream()
                .map(ItemMapper::toItemDtoWithId)
                .collect(toList());
    }

    @Override
    @Transactional
    public CommentDtoReturn addComment(Long userId, CommentDto commentDto, Long itemId) {

        Optional<User> userO = userRepository.findById(userId);

        if (userO.isEmpty()) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        User user = userO.get();

        Optional<Item> itemOpt = itemRepository.findById(itemId);

        if (itemOpt.isEmpty()) {
            throw new NotFoundException("Не найден айтем с id: " + itemId);
        }

        Item item = itemOpt.get();

        List<BookingDtoReturn> bookings = bookingRepository.findAllByUserAndItem(userId, itemId, LocalDateTime.now())
                .stream()
                .map(BookingMapper::bookingDtoReturnFromInterface)
                .collect(Collectors.toList());

        if (bookings.isEmpty()) {
            throw new CommonValidationException400("Не найдены букинги вещи " + itemId + " пользователем " + userId);
        }

        return CommentMapper.toCommentDtoReturn(commentRepository.save(CommentMapper.toComment(commentDto, item, user)));
    }

    public List<CommentDtoReturn> getAllCommentsByItemId(Long itemId) {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        return comments.stream()
                .map(CommentMapper::toCommentDtoReturn)
                .collect(Collectors.toList());
    }

    private BookingDtoReturn findItemLastBooking(List<BookingDtoReturn> bookingDtoReturnList, LocalDateTime localDateTime) {

        if (bookingDtoReturnList == null || bookingDtoReturnList.isEmpty()) {
            return null;
        }

        return bookingDtoReturnList.stream()
                .filter(bookingDtoReturn -> bookingDtoReturn.getStart().isBefore(localDateTime))
                .findFirst()
                .orElse(null);
    }

    private BookingDtoReturn findItemNextBooking(List<BookingDtoReturn> bookingDtoReturnList, LocalDateTime localDateTime) {

        if (bookingDtoReturnList == null || bookingDtoReturnList.isEmpty()) {
            return null;
        }

        return bookingDtoReturnList.stream()
                .filter(bookingDtoReturn -> bookingDtoReturn.getStart().isAfter(localDateTime))
                .findFirst()
                .orElse(null);
    }

    private BookingDtoReturn findItemLastBookingById(Long itemId, LocalDateTime localDateTime) {

        Optional<BookingDtoReturn> lastBookingO = bookingRepository.findLastBooking(itemId, localDateTime).map(BookingMapper::bookingDtoReturnFromInterface);

        return lastBookingO.orElse(null);

    }

    private BookingDtoReturn findItemNextBookingById(Long itemId, LocalDateTime localDateTime) {

        Optional<BookingDtoReturn> nextBookingO = bookingRepository.findNextBooking(itemId, localDateTime).map(BookingMapper::bookingDtoReturnFromInterface);

        return nextBookingO.orElse(null);

    }

}
