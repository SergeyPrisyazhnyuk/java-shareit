package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

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

        Optional<User> userO =  userRepository.findById(userId);

        if (userO.isEmpty()) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        User user = userO.get();


        System.out.println("***************************************************************");
        System.out.println("user.getId() "+ user.getId());
        System.out.println("***************************************************************");

        Optional<Item> itemO = itemRepository.findById(bookingDto.getItemId());

        if (itemO.isEmpty()) {
            throw new NotFoundException("Не найден айтем с id: " + bookingDto.getItemId());
        }

        Item item = itemO.get();

        bookingValidation(bookingDto, item);

        Booking booking = new Booking(
                                bookingDto.getStart(),
                                bookingDto.getEnd(),
                                item,
                                user,
                                BookingStatus.WAITING
        );

        bookingRepository.save(booking);

        System.out.println("***************************************************************");
        System.out.println("booking.getId(); " + booking.getId());
        System.out.println("***************************************************************");



        return BookingMapper.bookingDtoReturn(booking);
    }

    @Override
    public BookingDto updateBookingStatus(Long userId, Long bookingId, Boolean status) {
        return null;
    }

    @Override
    public BookingDto getBookingByUserId(Long userId, Long bookingId) {
        return null;
    }

    @Override
    public List<BookingDto> getAll(Long userId, String state) {
        return null;
    }

    @Override
    public List<BookingDto> getAllByOwner(Long userId, String state) {
        return null;
    }

    private void bookingValidation(BookingDto bookingDto, Item item) {
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

}
