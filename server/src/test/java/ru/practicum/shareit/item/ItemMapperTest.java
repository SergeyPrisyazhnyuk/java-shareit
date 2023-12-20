package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.item.dto.CommentDtoReturn;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    BookingDtoReturn lastBooking = new BookingDtoReturn(1L, 2L);

    BookingDtoReturn nextBooking = new BookingDtoReturn(1L, 3L);


    List<CommentDtoReturn> comments = new ArrayList<>();

    private ItemBookingDto empty = new ItemBookingDto();

    private ItemBookingDto itemBookingDto = new ItemBookingDto(1L, "itemBookingDto", "ItemBookingDtoDescription", true);

    private ItemBookingDto itemBookingDtoAll = new ItemBookingDto(2L, "itemBookingDto", "ItemBookingDtoDescription", true, lastBooking, nextBooking, comments);


    Item item  = new Item(1L, "itemName", "itemDescription", true, 1L, 2L);

    ItemDto itemDto = new ItemDto("itemDtoName", "itemDtoDescr", true, 2L, 3L);

    @Test
    void toItemDtoWithId() {
        ItemDto itemDto = ItemMapper.toItemDtoWithId(item);
        assertEquals(1L, itemDto.getId());
    }

    @Test
    void toItem() {
        Item item1 = ItemMapper.toItem(itemDto);
        assertEquals("itemDtoName", item1.getName());


    }

    @Test
    void toItemBookingDto() {
        ItemBookingDto itemBookingDto1 = ItemMapper.toItemBookingDto(item);
        assertEquals("itemName", itemBookingDto1.getName());

    }

    @Test
    void testToItemBookingDto() {
        ItemBookingDto itemBookingDto1 = ItemMapper.toItemBookingDto(item, lastBooking, nextBooking, comments);
        assertEquals("itemName", itemBookingDto1.getName());
        assertNotNull(itemBookingDto1.getLastBooking());
    }
}