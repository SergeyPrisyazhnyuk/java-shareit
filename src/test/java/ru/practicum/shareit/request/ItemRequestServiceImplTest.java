package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NotFoundException;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoReturn;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    User user = new User(1L, "user", "user@yandex.ru");

    ItemRequestDto itemRequestDto = new ItemRequestDto("description_of_Item");

    Item item = new Item(1L, "item", "itemdescription", true, user.getId(), 1L);

    List<Item> items = List.of(item);

    ItemRequest itemRequest = new ItemRequest(1L, "description", user, LocalDateTime.now().minusMinutes(2), items);




    @Test
    void save_whenInvoked_ThenOk() {

        ItemRequestDtoReturn itemRequestDtoR = new ItemRequestDtoReturn();
        itemRequestDtoR.setDescription("description_of_Item");
        itemRequestDtoR.setItems(new ArrayList<>());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ItemRequestDtoReturn itemRequestDtoReturn = itemRequestService.save(user.getId(), itemRequestDto);

        assertNotNull(itemRequestDtoReturn);
        assertEquals(itemRequestDtoR, itemRequestDtoReturn);

    }

    @Test
    void save_whenInvoked_ThenNotFoundException() {

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestService.save(user.getId(), itemRequestDto));

        assertEquals(notFoundException.getMessage(), "Не найден юзер с id: " + user.getId());

    }



    @Test
    void getRequestByUserId_whenInvoked_ThenOk() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequestorId(user.getId())).thenReturn(List.of(itemRequest));

        List<ItemRequestDtoReturn> list = itemRequestService.getRequestByUserId(user.getId());

        assertFalse(list.isEmpty());
        assertEquals("description", list.get(0).getDescription());

    }

    @Test
    void getRequestByUserId_whenInvoked_ThenNotFoundException() {

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestService.getRequestByUserId(user.getId()));

        assertEquals(notFoundException.getMessage(), "Не найден юзер с id: " + user.getId());

    }


    @Test
    void getAllRequestsFromToSize_whenInvoked_ThenOk() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequestorIdNotOrderByCreated(user.getId(), PageRequest.of(0,1))).thenReturn(List.of(itemRequest));

        List<ItemRequestDtoReturn> list = itemRequestService.getAllRequestsFromToSize(user.getId(), 0,1);

        assertNotNull(list);
    }

    @Test
    void getAllRequestsFromToSize_whenInvoked_ThenNotFoundException() {

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestService.getAllRequestsFromToSize(user.getId(), 0, 1));

        assertEquals(notFoundException.getMessage(), "Не найден юзер с id: " + user.getId());
    }

    @Test
    void getRequestById_whenInvoked_ThenOk() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));

        ItemRequestDtoReturn itemRequestDtoReturn = itemRequestService.getRequestById(user.getId(), itemRequest.getId());

        assertNotNull(itemRequestDtoReturn);
        assertEquals("description", itemRequestDtoReturn.getDescription());
    }

    @Test
    void getRequestById_whenInvoked__whenInvoked_ThenNotFoundException() {
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestService.getRequestById(user.getId(), itemRequest.getId()));

        assertEquals(notFoundException.getMessage(), "Не найден юзер с id: " + user.getId());
    }

    @Test
    void getRequestById_whenInvoked__whenInvoked_ThenRequestNotFoundException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestService.getRequestById(user.getId(), itemRequest.getId()));

        assertEquals(notFoundException.getMessage(), "Request with id " + itemRequest.getId() + " was not found");
    }

}