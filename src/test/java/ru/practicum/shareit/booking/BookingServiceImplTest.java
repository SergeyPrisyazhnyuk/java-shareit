package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInterface;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    BookingServiceImpl bookingService;

    LocalDateTime start = LocalDateTime.now().plusMinutes(2);
    LocalDateTime end = LocalDateTime.now().plusMinutes(10);


    User user = new User(1L, "user", "user@yandex.ru");

    User owner = new User(2L, "owner", "owner@yandex.ru");

    Item item = new Item(1L, "item", "itemdescription", true, owner.getId(), 1L);

    Booking booking = new Booking(1L, start, end, item, user, BookingStatus.APPROVED);

    Booking bookingW = new Booking(2L, start.plusMinutes(11), end.plusMinutes(15), item, user, BookingStatus.WAITING);

    BookingDto bookingDto = new BookingDto(item.getId(), start, end);

    BookingDto badBookingDto = new BookingDto(item.getId(), end, start);

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

        BookingDtoReturn bookingDtoReturn = BookingMapper.bookingDtoReturn(booking);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        BookingDtoReturn bookingDtoReturn1 = bookingService.save(user.getId(), bookingDto);
        bookingDtoReturn1.setId(user.getId());

        assertEquals(booking.getId(), bookingDtoReturn.getId());
        assertEquals(booking.getStart(), bookingDtoReturn1.getStart());
        assertEquals(booking.getId(), bookingDtoReturn1.getId());

    }

    @Test
    void save_whenInvoked_ThenUserNotFoundException() {

        NotFoundException bookingTimeException = assertThrows(NotFoundException.class,
                () -> bookingService.save(user.getId(), bookingDto));

        assertEquals(bookingTimeException.getMessage(), "Не найден юзер с id: " + user.getId());
    }

    @Test
    void save_whenInvoked_ThenThrowTimeException() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        BookingValidationException bookingTimeException = assertThrows(BookingValidationException.class,
                () -> bookingService.save(user.getId(), badBookingDto));

        assertEquals(bookingTimeException.getMessage(), "End of booking date can't be earlier then start date!");
    }

    @Test
    void save_whenInvoked_ThenThrowAvailableException() {

        item.setAvailable(false);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        BookingValidationException bookingTimeException = assertThrows(BookingValidationException.class,
                () -> bookingService.save(user.getId(), badBookingDto));

        assertEquals(bookingTimeException.getMessage(), "Item is not available!");
    }

    @Test
    void save_whenInvoked_ThenThrowTimeEqualException() {

        badBookingDto.setStart(LocalDateTime.now().plusMinutes(1));
        badBookingDto.setEnd(badBookingDto.getStart());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        BookingValidationException bookingTimeException = assertThrows(BookingValidationException.class,
                () -> bookingService.save(user.getId(), badBookingDto));

        assertEquals(bookingTimeException.getMessage(), "End of booking date can't be equal start date!");
    }

    @Test
    void updateBookingStatus_whenInvoked_ThenOk() {

        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingW));
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingW);

        BookingDtoReturn bookingDtoReturn = bookingService.updateBookingStatus(owner.getId(), bookingW.getId(), true);

        assertEquals(BookingStatus.APPROVED, bookingDtoReturn.getStatus());

    }

    @Test
    void updateBookingStatus_whenInvoked_ThenValidationException() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        CommonValidationException404 bookingTimeException = assertThrows(CommonValidationException404.class,
                () -> bookingService.updateBookingStatus(user.getId(), booking.getId(), true));

        assertEquals(bookingTimeException.getMessage(), "Current user is not owner");

    }

    @Test
    void updateBookingStatus_whenInvoked_ThenStatusException() {

        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        CommonValidationException400 bookingTimeException = assertThrows(CommonValidationException400.class,
                () -> bookingService.updateBookingStatus(owner.getId(), booking.getId(), true));

        assertEquals(bookingTimeException.getMessage(), "Current status is not WAITING");

    }

    @Test
    void getBookingByUserId() {

        BookingDtoReturn bookingDtoReturn = BookingMapper.bookingDtoReturn(booking);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingDtoReturn bookingDtoReturn1 = bookingService.getBookingByUserId(user.getId(), booking.getId());

        assertEquals(booking.getId(), bookingDtoReturn.getId());
        assertEquals(booking.getStart(), bookingDtoReturn1.getStart());
        assertEquals(booking.getId(), bookingDtoReturn1.getId());

    }

    @Test
    void getAll_findAllBookingsByBookerId() {
        List<BookingDtoReturn> bookingDtoReturn = List.of(BookingMapper.bookingDtoReturn(booking));
        Page<BookingDtoInterface> page = PageableExecutionUtils.getPage(List.of(bookingDtoInterface), PageRequest.of(0, 1), () -> List.of(bookingDtoInterface).size());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllBookingsByBookerId(anyLong(), any(Pageable.class))).thenReturn(page);


        List<BookingDtoReturn> bookingDtoReturn1 = bookingService.getAll(user.getId(), "ALL", 0, 1);

        assertEquals(bookingDtoReturn.get(0).getId(), bookingDtoReturn1.get(0).getId());
        assertEquals(bookingDtoReturn.get(0).getStart(), bookingDtoReturn1.get(0).getStart());

    }

    @Test
    void getAll_findCurrentBookingsByBookerId() {
        List<BookingDtoReturn> bookingDtoReturn = List.of(BookingMapper.bookingDtoReturn(booking));
        Page<BookingDtoInterface> page = PageableExecutionUtils.getPage(List.of(bookingDtoInterface), PageRequest.of(0, 1), () -> List.of(bookingDtoInterface).size());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findCurrentBookingsByBookerId(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);


        List<BookingDtoReturn> bookingDtoReturn1 = bookingService.getAll(user.getId(), "CURRENT", 0, 1);

        assertEquals(bookingDtoReturn.get(0).getId(), bookingDtoReturn1.get(0).getId());
        assertEquals(bookingDtoReturn.get(0).getStart(), bookingDtoReturn1.get(0).getStart());

    }

    @Test
    void getAll_findPastBookingsByBookerId() {
        List<BookingDtoReturn> bookingDtoReturn = List.of(BookingMapper.bookingDtoReturn(booking));
        Page<BookingDtoInterface> page = PageableExecutionUtils.getPage(List.of(bookingDtoInterface), PageRequest.of(0, 1), () -> List.of(bookingDtoInterface).size());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findPastBookingsByBookerId(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);


        List<BookingDtoReturn> bookingDtoReturn1 = bookingService.getAll(user.getId(), "PAST", 0, 1);

        assertEquals(bookingDtoReturn.get(0).getId(), bookingDtoReturn1.get(0).getId());
        assertEquals(bookingDtoReturn.get(0).getStart(), bookingDtoReturn1.get(0).getStart());

    }

    @Test
    void getAll_findFutureBookingsByBookerId() {
        List<BookingDtoReturn> bookingDtoReturn = List.of(BookingMapper.bookingDtoReturn(booking));
        Page<BookingDtoInterface> page = PageableExecutionUtils.getPage(List.of(bookingDtoInterface), PageRequest.of(0, 1), () -> List.of(bookingDtoInterface).size());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findFutureBookingsByBookerId(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);


        List<BookingDtoReturn> bookingDtoReturn1 = bookingService.getAll(user.getId(), "FUTURE", 0, 1);

        assertEquals(bookingDtoReturn.get(0).getId(), bookingDtoReturn1.get(0).getId());
        assertEquals(bookingDtoReturn.get(0).getStart(), bookingDtoReturn1.get(0).getStart());

    }

    @Test
    void getAll_findWaitingBookingsByBookerId() {
        List<BookingDtoReturn> bookingDtoReturn = List.of(BookingMapper.bookingDtoReturn(booking));
        Page<BookingDtoInterface> page = PageableExecutionUtils.getPage(List.of(bookingDtoInterface), PageRequest.of(0, 1), () -> List.of(bookingDtoInterface).size());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findWaitingBookingsByBookerId(anyLong(), any(Pageable.class))).thenReturn(page);


        List<BookingDtoReturn> bookingDtoReturn1 = bookingService.getAll(user.getId(), "WAITING", 0, 1);

        assertEquals(bookingDtoReturn.get(0).getId(), bookingDtoReturn1.get(0).getId());
        assertEquals(bookingDtoReturn.get(0).getStart(), bookingDtoReturn1.get(0).getStart());

    }

    @Test
    void getAll_findRejectedBookingsByBookerId() {
        List<BookingDtoReturn> bookingDtoReturn = List.of(BookingMapper.bookingDtoReturn(booking));
        Page<BookingDtoInterface> page = PageableExecutionUtils.getPage(List.of(bookingDtoInterface), PageRequest.of(0, 1), () -> List.of(bookingDtoInterface).size());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findRejectedBookingsByBookerId(anyLong(), any(Pageable.class))).thenReturn(page);


        List<BookingDtoReturn> bookingDtoReturn1 = bookingService.getAll(user.getId(), "REJECTED", 0, 1);

        assertEquals(bookingDtoReturn.get(0).getId(), bookingDtoReturn1.get(0).getId());
        assertEquals(bookingDtoReturn.get(0).getStart(), bookingDtoReturn1.get(0).getStart());

    }

    @Test
    void getAll_wrongState() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(WrongStateException.class,
                () -> bookingService.getAll(user.getId(), "ERROR", 0, 1));

    }

    @Test
    void getAllByOwner_findAllBookingsByOwnerId() {

        List<BookingDtoReturn> bookingDtoReturn = List.of(BookingMapper.bookingDtoReturn(booking));
        Page<BookingDtoInterface> page = PageableExecutionUtils.getPage(List.of(bookingDtoInterface), PageRequest.of(0, 1), () -> List.of(bookingDtoInterface).size());

        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllBookingsByOwnerId(anyLong(), any(Pageable.class))).thenReturn(page);


        List<BookingDtoReturn> bookingDtoReturn1 = bookingService.getAllByOwner(owner.getId(), "ALL", 0, 1);

        assertEquals(bookingDtoReturn.get(0).getId(), bookingDtoReturn1.get(0).getId());
        assertEquals(bookingDtoReturn.get(0).getStart(), bookingDtoReturn1.get(0).getStart());

    }

    @Test
    void getAllByOwner_findCurrentBookingsByOwnerId() {

        List<BookingDtoReturn> bookingDtoReturn = List.of(BookingMapper.bookingDtoReturn(booking));
        Page<BookingDtoInterface> page = PageableExecutionUtils.getPage(List.of(bookingDtoInterface), PageRequest.of(0, 1), () -> List.of(bookingDtoInterface).size());

        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findCurrentBookingsByOwnerId(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);


        List<BookingDtoReturn> bookingDtoReturn1 = bookingService.getAllByOwner(owner.getId(), "CURRENT", 0, 1);

        assertEquals(bookingDtoReturn.get(0).getId(), bookingDtoReturn1.get(0).getId());
        assertEquals(bookingDtoReturn.get(0).getStart(), bookingDtoReturn1.get(0).getStart());

    }

    @Test
    void getAllByOwner_findPastBookingsByOwnerId() {

        List<BookingDtoReturn> bookingDtoReturn = List.of(BookingMapper.bookingDtoReturn(booking));
        Page<BookingDtoInterface> page = PageableExecutionUtils.getPage(List.of(bookingDtoInterface), PageRequest.of(0, 1), () -> List.of(bookingDtoInterface).size());

        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findPastBookingsByOwnerId(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);


        List<BookingDtoReturn> bookingDtoReturn1 = bookingService.getAllByOwner(owner.getId(), "PAST", 0, 1);

        assertEquals(bookingDtoReturn.get(0).getId(), bookingDtoReturn1.get(0).getId());
        assertEquals(bookingDtoReturn.get(0).getStart(), bookingDtoReturn1.get(0).getStart());

    }

    @Test
    void getAllByOwner_findFutureBookingsByOwnerId() {

        List<BookingDtoReturn> bookingDtoReturn = List.of(BookingMapper.bookingDtoReturn(booking));
        Page<BookingDtoInterface> page = PageableExecutionUtils.getPage(List.of(bookingDtoInterface), PageRequest.of(0, 1), () -> List.of(bookingDtoInterface).size());

        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findFutureBookingsByOwnerId(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);


        List<BookingDtoReturn> bookingDtoReturn1 = bookingService.getAllByOwner(owner.getId(), "FUTURE", 0, 1);

        assertEquals(bookingDtoReturn.get(0).getId(), bookingDtoReturn1.get(0).getId());
        assertEquals(bookingDtoReturn.get(0).getStart(), bookingDtoReturn1.get(0).getStart());

    }

    @Test
    void getAllByOwner_findWaitingBookingsByOwnerId() {

        List<BookingDtoReturn> bookingDtoReturn = List.of(BookingMapper.bookingDtoReturn(booking));
        Page<BookingDtoInterface> page = PageableExecutionUtils.getPage(List.of(bookingDtoInterface), PageRequest.of(0, 1), () -> List.of(bookingDtoInterface).size());

        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findWaitingBookingsByOwnerId(anyLong(), any(Pageable.class))).thenReturn(page);


        List<BookingDtoReturn> bookingDtoReturn1 = bookingService.getAllByOwner(owner.getId(), "WAITING", 0, 1);

        assertEquals(bookingDtoReturn.get(0).getId(), bookingDtoReturn1.get(0).getId());
        assertEquals(bookingDtoReturn.get(0).getStart(), bookingDtoReturn1.get(0).getStart());

    }

    @Test
    void getAllByOwner_findRejectedBookingsByOwnerId() {

        List<BookingDtoReturn> bookingDtoReturn = List.of(BookingMapper.bookingDtoReturn(booking));
        Page<BookingDtoInterface> page = PageableExecutionUtils.getPage(List.of(bookingDtoInterface), PageRequest.of(0, 1), () -> List.of(bookingDtoInterface).size());

        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findRejectedBookingsByOwnerId(anyLong(), any(Pageable.class))).thenReturn(page);


        List<BookingDtoReturn> bookingDtoReturn1 = bookingService.getAllByOwner(owner.getId(), "REJECTED", 0, 1);

        assertEquals(bookingDtoReturn.get(0).getId(), bookingDtoReturn1.get(0).getId());
        assertEquals(bookingDtoReturn.get(0).getStart(), bookingDtoReturn1.get(0).getStart());

    }

    @Test
    void getAllByOwner_wrongState() {

        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        assertThrows(WrongStateException.class,
                () -> bookingService.getAllByOwner(owner.getId(), "ERROR", 0, 1));

    }

}