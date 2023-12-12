package ru.practicum.shareit.itemrequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoReturn;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;


    Long requestId = 0L;
    Long userId = 1L;
    Integer from = 0;
    Integer size = 1;

    @Test
    void save_whenInvoked_thenItemRequestExists() {
        ItemRequestDtoReturn itemRequestDtoExpected = new ItemRequestDtoReturn();
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        Mockito.when(itemRequestService.save(userId,itemRequestDto)).thenReturn(itemRequestDtoExpected);

        ItemRequestDtoReturn item = itemRequestController.save(userId, itemRequestDto);

        assertEquals(itemRequestDtoExpected, item);

    }

    @Test
    void getRequestByUserId_whenInvoked_thenItemRequestExists() {

        List<ItemRequestDtoReturn> itemRequestDtoReturn = List.of(new ItemRequestDtoReturn());

        Mockito.when(itemRequestService.getRequestByUserId(userId)).thenReturn(itemRequestDtoReturn);

        List<ItemRequestDtoReturn> itemRequest = itemRequestController.getRequestByUserId(userId);

        assertEquals(itemRequestDtoReturn, itemRequest);

    }

    @Test
    void getAllRequestsFromToSize_whenInvoked_thenItemRequestExists() {

        List<ItemRequestDtoReturn> itemRequestDtoReturn = List.of(new ItemRequestDtoReturn());

        Mockito.when(itemRequestService.getAllRequestsFromToSize(userId, from, size)).thenReturn(itemRequestDtoReturn);

        List<ItemRequestDtoReturn> itemRequest = itemRequestController.getAllRequestsFromToSize(userId, from, size);

        assertEquals(itemRequestDtoReturn, itemRequest);

    }

    @Test
    void getRequestById_whenInvoked_thenItemRequestExists() {

        ItemRequestDtoReturn itemRequestDtoReturn = new ItemRequestDtoReturn();

        Mockito.when(itemRequestService.getRequestById(userId, requestId)).thenReturn(itemRequestDtoReturn);

        ItemRequestDtoReturn itemRequest = itemRequestController.getRequestById(userId, requestId);

        assertEquals(itemRequestDtoReturn, itemRequest);

    }
}