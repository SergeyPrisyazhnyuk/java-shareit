package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoReturn;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    User user = new User(1L, "user", "user@yandex.ru");

    User owner = new User(2L, "owner", "owner@yandex.ru");

    Item item = new Item(1L, "item", "itemdescription", true, owner.getId(), 1L);

    List<Item> items = List.of(item);

    ItemRequestDto itemRequestDto = new ItemRequestDto("description_of_Item");

    ItemRequest itemRequest = new ItemRequest(1L, "description", user, LocalDateTime.now().minusMinutes(2), items);



    @Test
    void toItemRequest() {
        ItemRequest itemRequest1 = ItemRequestMapper.toItemRequest(itemRequestDto);

        assertNotNull(itemRequest1);
        assertEquals("description_of_Item", itemRequest1.getDescription());
    }

    @Test
    void toItemRequestDtoReturn_AllFields() {

        ItemRequestDtoReturn itemRequestDtoReturn = ItemRequestMapper.toItemRequestDtoReturn(itemRequest);

        assertNotNull(itemRequestDtoReturn);
        assertEquals("description", itemRequestDtoReturn.getDescription());
    }

    @Test
    void toItemRequestDtoReturn_NoItemList() {

        itemRequest.setItems(Collections.emptyList());
        ItemRequestDtoReturn itemRequestDtoReturn = ItemRequestMapper.toItemRequestDtoReturn(itemRequest);

        assertNotNull(itemRequestDtoReturn);
        assertEquals("description", itemRequestDtoReturn.getDescription());
    }


}