package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongUserException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoReturn;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    Long itemId = 0L;
    Long userId = 1L;
    String text = "Mama mila Ramu, Rama crasnel so sramu";

    @Test
    void save_whenInvoked_thenItemExists() {
        ItemDto itemDtoExpected = new ItemDto();
        ItemDto itemDto = new ItemDto();

        Mockito.when(itemService.save(userId,itemDto)).thenReturn(itemDtoExpected);

        ItemDto item = itemController.save(userId, itemDto);

        assertEquals(itemDtoExpected, item);


    }

    @Test
    void update_whenInvoked_thenItemExists() {

        ItemDto itemDtoExpected = new ItemDto();
        ItemDto itemDto = new ItemDto();

        Mockito.when(itemService.update(userId, itemId, itemDto)).thenReturn(itemDtoExpected);

        ItemDto item = itemController.update(userId, itemId, itemDto);

        assertEquals(itemDtoExpected, item);

    }

    @Test
    void getItemById_whenInvoked_thenItemExists() {

        ItemDto itemDtoExpected = new ItemDto();

        Mockito.when(itemService.getItemById(userId, itemId)).thenReturn(itemDtoExpected);

        ItemDto item = itemController.getItemById(userId, itemId);

        assertEquals(itemDtoExpected, item);

    }

    @Test
    void getAll_whenInvoked_thenItemExists() {

        List<ItemBookingDto> itemDtoExpectedList = List.of(new ItemBookingDto());

        Mockito.when(itemService.getAll(userId)).thenReturn(itemDtoExpectedList);

        List<ItemBookingDto> item = itemController.getAll(userId);

        assertEquals(itemDtoExpectedList, item);


    }

    @Test
    void deleteItemById_whenInvoked_thenOk() {
        ItemController myList = mock(ItemController.class);
        doNothing().when(myList).deleteItemById(isA(Long.class), isA(Long.class));
        myList.deleteItemById(0L, 1L);

        verify(myList, times(1)).deleteItemById(0L, 1L);

    }

    @Test
    void search_whenInvoked_thenItemExists() {

        List<ItemDto> itemDtoExpectedList = List.of(new ItemDto());

        Mockito.when(itemService.search(userId, text)).thenReturn(itemDtoExpectedList);

        List<ItemDto> item = itemController.search(userId, text);

        assertEquals(itemDtoExpectedList, item);

    }

    @Test
    void addComment_whenInvoked_thenCommentExists() {

        CommentDtoReturn commentExpected = new CommentDtoReturn();
        CommentDto commentDto = new CommentDto();

        Mockito.when(itemService.addComment(userId, commentDto,itemId)).thenReturn(commentExpected);

        CommentDtoReturn comment = itemController.addComment(userId, commentDto,itemId);

        assertEquals(commentExpected, comment);



    }
}