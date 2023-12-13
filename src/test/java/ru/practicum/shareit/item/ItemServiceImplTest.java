package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoInterface;
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

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    Comment comment = new Comment(1L, "texttext", item, user, LocalDateTime.now().minusMinutes(1));

    CommentDtoReturn commentDtoR = CommentMapper.toCommentDtoReturn(comment);

    BookingDtoInterface bookingDtoInterface = new BookingDtoInterface() {

        @Override
        public Long getBookingId() {
            return 1L;
        }

        @Override
        public LocalDateTime getBookingStartDate() {
            return start;
        }

        @Override
        public LocalDateTime getBookingEndDate() {
            return end;
        }

        @Override
        public BookingStatus getBookingStatus() {
            return BookingStatus.APPROVED;
        }

        @Override
        public Long getBookingItemId() {
            return 1L;
        }

        @Override
        public String getBookingItemName() {
            return "item";
        }

        @Override
        public Long getBookingBookerId() {
            return 2L;
        }
    };

    @Test
    void save_whenInvoked_ThenOk() {
        ItemDto itemDto = ItemMapper.toItemDtoWithId(item);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        ItemDto itemDtoResult = itemService.save(owner.getId(), itemDto);
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

        ItemDto itemDtoResult = itemService.update(owner.getId(), item.getId(), itemDto);
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

        ItemDto itemDtoResult = itemService.update(owner.getId(), item.getId(), itemDto);
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

        ItemDto itemDtoResult = itemService.getItemById(owner.getId(), item.getId());
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
    void getAll_whenInvoked_ThenOk() {
        List<Long> itemIdList = List.of(item.getId());

        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findAllByOwnerId(owner.getId())).thenReturn(List.of(item));
        when(bookingRepository.findAllByItemInAndStatusOrderByStartAsc(anyList(), any(BookingStatus.class))).thenReturn(List.of(bookingDtoInterface));

        when(commentRepository.findAllByItemIdIn(itemIdList)).thenReturn(List.of(comment));
        Map<Long, List<CommentDtoReturn>> commentsMap = new HashMap<>();
        commentsMap.put(1L, commentRepository.findAllByItemIdIn(itemIdList).stream().map(CommentMapper::toCommentDtoReturn).collect(Collectors.toList()));

        List<BookingDtoInterface> bookingDtoReturnList = bookingRepository.findAllByItemInAndStatusOrderByStartAsc(anyList(), any(BookingStatus.class));
        List<BookingDtoReturn> bookingDtoReturns = bookingDtoReturnList.stream().map(BookingMapper::bookingDtoReturnFromInterface).collect(Collectors.toList());
        Map<Long, List<BookingDtoReturn>> bookingsMap = new HashMap<>();
        bookingsMap.put(1L, bookingDtoReturns);

        List<ItemBookingDto> itemBookingDtos = itemService.getAll(owner.getId());

        assertFalse(commentsMap.isEmpty());
        assertFalse(bookingsMap.isEmpty());
        assertFalse(itemBookingDtos.isEmpty());

    }

    @Test
    void getAll_whenInvoked_NoBookings_ThenOk() {
        List<Long> itemIdList = List.of(item.getId());

        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findAllByOwnerId(owner.getId())).thenReturn(List.of(item));
        when(bookingRepository.findAllByItemInAndStatusOrderByStartAsc(anyList(), any(BookingStatus.class))).thenReturn(List.of(bookingDtoInterface));

        Map<Long, List<BookingDtoReturn>> bookingDtoReturnMap = new HashMap<>();

        List<ItemBookingDto> itemBookingDtos = itemService.getAll(owner.getId());

        List<ItemBookingDto> itemDtos = new ArrayList<>();

        if (bookingDtoReturnMap.isEmpty()) {
            itemDtos = itemRepository.findAllByOwnerId(owner.getId())
                    .stream()
                    .map(ItemMapper::toItemBookingDto)
                    .collect(toList());
        }

        assertTrue(bookingDtoReturnMap.isEmpty());
        assertFalse(itemBookingDtos.isEmpty());
        assertFalse(itemDtos.isEmpty());

    }

    @Test
    void getAll_whenInvoked_UserNotFoundException() {

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.getAll(user.getId()));

        assertEquals(notFoundException.getMessage(), "Не найден юзер с id: " + user.getId());
    }


    @Test
    void deleteItemById() {

        ItemServiceImpl myList = mock(ItemServiceImpl.class);
        doNothing().when(myList).deleteItemById(isA(Long.class), isA(Long.class));
        myList.deleteItemById(0L, 1L);

        verify(myList, times(1)).deleteItemById(0L, 1L);

    }

    @Test
    void deleteItemById_whenInvoked_UserNotFoundException() {
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.deleteItemById(user.getId(), item.getId()));

        assertEquals(notFoundException.getMessage(), "Не найден юзер с id: " + user.getId());

    }

    @Test
    void deleteItemById_whenInvoked_ItemNotFoundException() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.deleteItemById(owner.getId(), item2.getId()));
    }

    @Test
    void deleteItemById_whenInvoked_WrongUserException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        WrongUserException wrongUserException = assertThrows(WrongUserException.class,
                () -> itemService.deleteItemById(user.getId(), item.getId()));

        assertEquals(wrongUserException.getMessage(), "Пользователь с id " + user.getId() + " не является владельцем данной вещи и не может ее редактировать");
    }


    @Test
    void search_whenInvoked_ThenOk() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.search(anyString())).thenReturn(List.of(item));

        List<ItemDto> searchResut = itemService.search(owner.getId(), "blablabla");
        System.out.println(searchResut);

        assertFalse(searchResut.isEmpty());
    }

    @Test
    void search_whenInvoked_BlankText_ThenOk() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        List<ItemDto> searchResut = itemService.search(owner.getId(), "");

        assertFalse(!searchResut.isEmpty());
    }

    @Test
    void search_whenInvoked_UserNotFoundException() {
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.search(user.getId(), "sometext"));

        assertEquals(notFoundException.getMessage(), "Не найден юзер с id: " + user.getId());

    }


    @Test
    void addComment_whenInvoked_ThenOk() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByUserAndItem(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(List.of(bookingDtoInterface));

        CommentDto commentDto = new CommentDto("blablabla");
        comment.setText(commentDto.getText());
        when(commentRepository.save(CommentMapper.toComment(commentDto, item, user))).thenReturn(comment);

        CommentDtoReturn commentDtoReturn = itemService.addComment(user.getId(), commentDto, item.getId());

        assertNotNull(commentDtoReturn);
        assertEquals("blablabla", commentDtoReturn.getText());
    }

    @Test
    void addComment_whenInvoked_UserNotFoundException() {
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.addComment(user.getId(), new CommentDto(), item.getId()));

        assertEquals(notFoundException.getMessage(), "Не найден юзер с id: " + user.getId());
    }

    @Test
    void addComment_whenInvoked_ItemNotFoundException() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.addComment(owner.getId(), new CommentDto(), item2.getId()));
    }

    @Test
    void addComment_whenInvoked_CommonValidationException400() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(CommonValidationException400.class,
                () -> itemService.addComment(user.getId(), new CommentDto(), item.getId()));
    }

    @Test
    void deleteItemById_ThenOk() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        itemService.deleteItemById(owner.getId(), item.getId());

    }
}