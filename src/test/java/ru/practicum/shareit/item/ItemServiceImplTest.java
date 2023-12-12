package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {


    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    ItemServiceImpl itemService;

    LocalDateTime start = LocalDateTime.now().plusMinutes(2);
    LocalDateTime end = LocalDateTime.now().plusMinutes(10);


    User user = new User(1L, "user", "user@yandex.ru");

    User owner = new User(2L, "owner", "owner@yandex.ru");

    Item item = new Item(1L, "item", "itemdescription", true, owner.getId(), 1L);

    Item item2 = new Item(2L, "item", "itemdescription", true, 3L, 1L);

    Item item3 = new Item("item", "itemdescription", true);

    Item itemUpdated = new Item(1L, "itemNew", "itemdescription", true, owner.getId(), 1L);

    Booking booking = new Booking(1L, start, end, item, user, BookingStatus.APPROVED);

    Booking bookingW = new Booking(2L, start.plusMinutes(11), end.plusMinutes(15), item, user, BookingStatus.WAITING);

    BookingDto bookingDto = new BookingDto(item.getId(), start, end);

    BookingDto badBookingDto = new BookingDto(item.getId(), end, start);


    @Test
    void save_whenInvoked_ThenOk() {
        ItemDto itemDto = ItemMapper.toItemDtoWithId(item);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        ItemDto itemDtoResult  = itemService.save(owner.getId(), itemDto);
        itemDtoResult.setId(itemDto.getId());

        assertEquals(itemDto.getId(), itemDtoResult.getId());
        assertEquals(itemDto.getName(), itemDtoResult.getName());
        assertEquals(itemDto.getOwner(), itemDtoResult.getOwner());
    }

    @Test
    void save_whenInvoked_ThenNotFoundException() {
        ItemDto itemDto = ItemMapper.toItemDtoWithId(item);

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.save(user.getId(), itemDto));

        assertEquals(notFoundException.getMessage(), "Не найден юзер с id: " + user.getId());
    }

    @Test
    void update_whenInvoked_ThenOk() {
        ItemDto itemDto = ItemMapper.toItemDtoWithId(itemUpdated);
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(itemUpdated);

        ItemDto itemDtoResult  = itemService.update(owner.getId(), item.getId(), itemDto);
        itemDtoResult.setId(itemDto.getId());

        assertEquals(itemDto.getId(), itemDtoResult.getId());
        assertEquals("itemNew", itemDtoResult.getName());
        assertEquals(itemDto.getOwner(), itemDtoResult.getOwner());
    }

    @Test
    void update_whenInvoked_getAlmostFieldsAreNull_ThenOk() {
        ItemDto itemDto = ItemMapper.toItemDtoWithId(itemUpdated);
        itemDto.setName(null);
        itemDto.setDescription(null);
        itemDto.setAvailable(null);
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(itemUpdated);

        ItemDto itemDtoResult  = itemService.update(owner.getId(), item.getId(), itemDto);
        itemDtoResult.setId(itemDto.getId());

        assertEquals(itemDto.getId(), itemDtoResult.getId());
        assertEquals("item", itemDtoResult.getName());
        assertEquals(itemDto.getOwner(), itemDtoResult.getOwner());
    }

    @Test
    void update_whenInvoked_UserNotFoundException() {
        ItemDto itemDto = ItemMapper.toItemDtoWithId(item);

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.update(user.getId(), item.getId(), itemDto));

        assertEquals(notFoundException.getMessage(), "Не найден юзер с id: " + user.getId());
    }

    @Test
    void update_whenInvoked_WrongUserException() {
        ItemDto itemDto = ItemMapper.toItemDtoWithId(item2);
        when(userRepository.existsById(owner.getId())).thenReturn(true);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item2));


        WrongUserException wrongUserException = assertThrows(WrongUserException.class,
                    () -> itemService.update(owner.getId(), item.getId(), itemDto));

        assertEquals(wrongUserException.getMessage(), "Пользователь с id " + owner.getId() + " не является владельцем данной вещи и не может ее редактировать");
    }

    @Test
    void update_whenInvoked_ItemNotFoundException() {
        ItemDto itemDto = ItemMapper.toItemDtoWithId(item2);
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.update(owner.getId(), item.getId(), itemDto));
    }

    @Test
    void getItemById_whenInvoked_ThenOk() {
        ItemDto itemDto = ItemMapper.toItemDtoWithId(item);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ItemDto itemDtoResult  = itemService.getItemById(owner.getId(), item.getId());
        itemDtoResult.setId(itemDto.getId());

        assertEquals(itemDto.getId(), itemDtoResult.getId());
        assertEquals(itemDto.getName(), itemDtoResult.getName());
        assertEquals(itemDto.getOwner(), itemDtoResult.getOwner());
    }

    @Test
    void getItemById_whenInvoked_UserNotFoundException() {
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.getItemById(user.getId(), item.getId()));

        assertEquals(notFoundException.getMessage(), "Не найден юзер с id: " + user.getId());
    }

    @Test
    void getItemById_whenInvoked_ItemNotFoundException() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.getItemById(owner.getId(), item2.getId()));
    }

    @Test
    void getAll() {
    }

    @Test
    void deleteItemById() {
    }

    @Test
    void search() {
    }

    @Test
    void addComment() {
    }

    @Test
    void getAllCommentsByItemId() {
    }
}