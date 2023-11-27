package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.exception.CommonValidationException400;
import ru.practicum.shareit.exception.CommonValidationException404;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongStateException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDtoReturn save(Long userId, BookingDto bookingDto) {

        User user = userValidation(userId);

        Optional<Item> itemO = itemRepository.findById(bookingDto.getItemId());

        if (itemO.isEmpty()) {
            throw new NotFoundException("Не найден айтем с id: " + bookingDto.getItemId());
        }

        Item item = itemO.get();

        if (userId.equals(item.getOwner())) {
            throw new CommonValidationException404("Нельзя брнировать свою же вещь");
        }

        bookingDtoValidation(bookingDto, item);

        Booking booking = new Booking(
                                bookingDto.getStart(),
                                bookingDto.getEnd(),
                                item,
                                user,
                                BookingStatus.WAITING
        );

        bookingRepository.save(booking);

        return BookingMapper.bookingDtoReturn(booking);
    }

    @Override
    @Transactional
    public BookingDtoReturn updateBookingStatus(Long userId, Long bookingId, Boolean status) {

        Booking booking = getBookingByBookingId(bookingId);

        userValidation(userId);

        bookingValidationUpdate(booking, userId);

        booking.setStatus(status ? BookingStatus.APPROVED : BookingStatus.REJECTED );

        bookingRepository.save(booking);

        return BookingMapper.bookingDtoReturn(booking);
    }

    @Override
    @Transactional
    public BookingDtoReturn getBookingByUserId(Long userId, Long bookingId) {

        userValidation(userId);

        Booking booking = getBookingByBookingId(bookingId);

        if (!booking.getBooker().getId().equals(userId)
                && !booking.getItem().getOwner().equals(userId)
            ) {
            throw new CommonValidationException404("Current user is neither the item owner nor the booker");
        }

        return BookingMapper.bookingDtoReturn(booking);
    }

    @Override
    @Transactional
    public List<BookingDtoReturn> getAll(Long bookerId, String state) {

        if (userRepository.findById(bookerId).isEmpty()) {
            throw new NotFoundException("Не найден юзер с id: " + bookerId);
        }

            bookingStateValidation(state);

        switch (state) {
            case "ALL" :
                return bookingRepository.findAllBookingsByBookerId(bookerId).stream()
                        .map(BookingMapper::bookingDtoReturn)
                        .collect(Collectors.toList());

            case "CURRENT" :
                return bookingRepository.findCurrentBookingsByBookerId(bookerId, LocalDateTime.now()).stream()
                        .map(BookingMapper::bookingDtoReturn)
                        .collect(Collectors.toList());

            case "PAST" :
                return bookingRepository.findPastBookingsByBookerId(bookerId, LocalDateTime.now()).stream()
                        .map(BookingMapper::bookingDtoReturn)
                        .collect(Collectors.toList());

            case "FUTURE" :
                return bookingRepository.findFutureBookingsByBookerId(bookerId, LocalDateTime.now()).stream()
                        .map(BookingMapper::bookingDtoReturn)
                        .collect(Collectors.toList());

            case "WAITING" :
                return bookingRepository.findWaitingBookingsByBookerId(bookerId).stream()
                        .map(BookingMapper::bookingDtoReturn)
                        .collect(Collectors.toList());

            case "REJECTED" :
                return bookingRepository.findRejectedBookingsByBookerId(bookerId).stream()
                        .map(BookingMapper::bookingDtoReturn)
                        .collect(Collectors.toList());

            default: throw new WrongStateException("Unknown state: " + state);

            }
    }

    @Override
    @Transactional
    public List<BookingDtoReturn> getAllByOwner(Long ownerId, String state) {
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException("Не найден юзер с id: " + ownerId);
        }

        bookingStateValidation(state);

        switch (state) {
            case "ALL" :
                return bookingRepository.findAllBookingsByOwnerId(ownerId).stream()
                        .map(BookingMapper::bookingDtoReturn)
                        .collect(Collectors.toList());

            case "CURRENT" :
                return bookingRepository.findCurrentBookingsByOwnerId(ownerId, LocalDateTime.now()).stream()
                        .map(BookingMapper::bookingDtoReturn)
                        .collect(Collectors.toList());

            case "PAST" :
                return bookingRepository.findPastBookingsByOwnerId(ownerId, LocalDateTime.now()).stream()
                        .map(BookingMapper::bookingDtoReturn)
                        .collect(Collectors.toList());

            case "FUTURE" :
                return bookingRepository.findFutureBookingsByOwnerId(ownerId, LocalDateTime.now()).stream()
                        .map(BookingMapper::bookingDtoReturn)
                        .collect(Collectors.toList());

            case "WAITING" :
                return bookingRepository.findWaitingBookingsByOwnerId(ownerId).stream()
                        .map(BookingMapper::bookingDtoReturn)
                        .collect(Collectors.toList());

            case "REJECTED" :
                return bookingRepository.findRejectedBookingsByOwnerId(ownerId).stream()
                        .map(BookingMapper::bookingDtoReturn)
                        .collect(Collectors.toList());

            default: throw new WrongStateException("Unknown state: " + state);
        }
    }



    private Booking getBookingByBookingId(Long bookingId) {
        Optional<Booking> bookingO = bookingRepository.findById(bookingId);

        if (bookingO.isEmpty()) {
            throw new NotFoundException("Не найден букинг с id: " + bookingId);
        }
        return bookingO.get();
    }

    private User userValidation(Long userId) {
        Optional<User> userO =  userRepository.findById(userId);

        if (userO == null || userO.isEmpty()) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        return userO.get();
    }

    private void bookingValidationUpdate(Booking booking, Long userId) {

        if (!booking.getItem().getOwner().equals(userId)) {
            throw new CommonValidationException404("Current user is not owner");
        }

        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new CommonValidationException400("Current status is not WAITING");
        }
    }

    private void bookingDtoValidation(BookingDto bookingDto, Item item) {

        if (!item.getAvailable()) {
            throw new BookingValidationException("Item is not available!");
        }

        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BookingValidationException("End of booking date can't be earlier then start date!");
        }

        if (bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            throw new BookingValidationException("End of booking date can't be equal start date!");
        }
    }

    public void bookingStateValidation(String state)  {
        BookingState bookingState = BookingState.getState(state);
        if (bookingState == null) {
            throw new WrongStateException("Unknown state: " + state);
        }
    }

}
