package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
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
        ItemDto itemDto = ItemMapper.toItemDtoWithId(item);

        if (!userId.equals(itemDto.getOwner())) {
            return itemDto;
        }

        List<Booking> bookings = bookingRepository.findAllByItemAndStatusOrderByStartAsc(item , BookingStatus.APPROVED);

        List<BookingDtoReturn> bookingDtoReturnList = bookings.stream()
                .map(BookingMapper::bookingDtoReturn)
                .collect(toList());

        System.out.println("*********************** bookingDtoReturnList       ********************************");
        System.out.println(bookingDtoReturnList);
        System.out.println("***********************************************************************************");

        System.out.println("*********************** bookingDtoReturn    LAST   ********************************");
        System.out.println(findItemLastBooking(bookingDtoReturnList,LocalDateTime.now()));
        System.out.println("***********************************************************************************");

        System.out.println("*********************** bookingDtoReturn    NEXT   ********************************");
        System.out.println(findItemNextBooking(bookingDtoReturnList,LocalDateTime.now()));
        System.out.println("***********************************************************************************");


        itemDto.setLastBooking(findItemLastBooking(bookingDtoReturnList,LocalDateTime.now()));
        itemDto.setNextBooking(findItemNextBooking(bookingDtoReturnList,LocalDateTime.now()));

        System.out.println("*********************** itemDto                    ********************************");
        System.out.println(itemDto);
        System.out.println("***********************************************************************************");

        return itemDto;
    }

    @Override
    @Transactional
    public List<ItemBookingDto> getAll(Long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

/*
        List<Item> itemList = itemRepository.findAllByOwnerId(userId)
                .stream()
                .sorted(Comparator.comparing(Item::getId))
                .collect(Collectors.toList());

        List<Booking> bookings = bookingRepository.findAllByItemInAndStatusOrderByStartAsc(itemList , BookingStatus.APPROVED);

        List<BookingDtoReturn> bookingDtoReturnList = bookings.stream()
                .map(BookingMapper::bookingDtoReturn)
                .collect(Collectors.toList());

        System.out.println("**************    itemList   ************************");
        System.out.println(itemList);

        System.out.println("**************    bookings   ************************");
        System.out.println(bookings);

        System.out.println("**************    bookingDtoReturnList   ************************");
        System.out.println(bookingDtoReturnList);


        System.out.println("**************    findItemLastBooking(bookingDtoReturnList, LocalDateTime.now())   ************************");
        System.out.println(findItemLastBooking(bookingDtoReturnList, LocalDateTime.now()));

        System.out.println("**************    findItemNextBooking(bookingDtoReturnList, LocalDateTime.now())   ************************");
        System.out.println(findItemNextBooking(bookingDtoReturnList, LocalDateTime.now()));


        List<ItemBookingDto> itemDtos = new ArrayList<>();

        if (bookingDtoReturnList.isEmpty()) {
            itemDtos = itemList
                    .stream()
                    .map(ItemMapper::toItemBookingDto)
                    .collect(Collectors.toList());
        } else {

            itemDtos = itemList
                    .stream()
                    .map(item -> ItemMapper.toItemBookingDto(
                            item,
                            findItemLastBooking(bookingDtoReturnList, LocalDateTime.now()),
                            findItemNextBooking(bookingDtoReturnList, LocalDateTime.now())
                    ))
                    .collect(Collectors.toList());
        }
*/
        List<Item> itemList = itemRepository.findAllByOwnerId(userId)
                .stream()
                .sorted(Comparator.comparing(Item::getId))
                .collect(Collectors.toList());

        Map<Long, List<BookingDtoReturn>> bookingDtoReturnMap = bookingRepository.findAllByItemInAndStatusOrderByStartAsc(itemList, BookingStatus.APPROVED)
                .stream()
                .map(BookingMapper::bookingDtoReturn)
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
                            findItemNextBooking(bookingDtoReturnMap.get(item.getId()), LocalDateTime.now())

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

        List<ItemDto> itemDtos = itemList
                .stream()
                .map(ItemMapper::toItemDtoWithId)
                .collect(toList());

        return itemDtos;
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


}
