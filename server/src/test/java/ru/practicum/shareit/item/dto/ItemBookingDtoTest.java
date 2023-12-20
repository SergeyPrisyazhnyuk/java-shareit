package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class ItemBookingDtoTest {

    @Autowired
    private JacksonTester<ItemBookingDto> json;

    BookingDtoReturn lastBooking = new BookingDtoReturn(1L, 2L);

    BookingDtoReturn nextBooking = new BookingDtoReturn(1L, 3L);


    List<CommentDtoReturn> comments = new ArrayList<>();

    private ItemBookingDto empty = new ItemBookingDto();

    private ItemBookingDto itemBookingDto = new ItemBookingDto(1L, "itemBookingDto", "ItemBookingDtoDescription", true);

    private ItemBookingDto itemBookingDtoAll = new ItemBookingDto(2L, "itemBookingDto", "ItemBookingDtoDescription", true, lastBooking, nextBooking, comments);

    @Test
    @SneakyThrows
    public void testId() {
        JsonContent<ItemBookingDto> jsonContent = this.json.write(itemBookingDto);
        jsonContent.assertThat().hasJsonPath("@.id", 1);
    }

    @Test
    @SneakyThrows
    public void testName() {
        JsonContent<ItemBookingDto> jsonContent = this.json.write(itemBookingDto);
        jsonContent.assertThat().hasJsonPathStringValue("@.name", "itemBookingDto");
    }

    @Test
    @SneakyThrows
    public void testDescription() {
        JsonContent<ItemBookingDto> jsonContent = this.json.write(itemBookingDto);
        jsonContent.assertThat().hasJsonPathStringValue("@.description", "ItemBookingDtoDescription");
    }

    @Test
    @SneakyThrows
    public void testAvailable() {
        JsonContent<ItemBookingDto> jsonContent = this.json.write(itemBookingDto);
        System.out.println(jsonContent);
        jsonContent.assertThat().hasJsonPathBooleanValue("@.available", true);
    }

    @Test
    void getId() {
        assertTrue(itemBookingDto.getId() != 0);
    }

    @Test
    void getName() {
        assertFalse(itemBookingDto.getName().isEmpty());
    }

    @Test
    void getDescription() {
        assertFalse(itemBookingDto.getDescription().isEmpty());
    }

    @Test
    void getAvailable() {
        assertEquals(true, itemBookingDto.getAvailable());

    }

    @Test
    void getLastBooking() {
        assertNotNull(itemBookingDtoAll.getLastBooking());
    }

    @Test
    void getNextBooking() {
        assertNotNull(itemBookingDtoAll.getNextBooking());
    }

    @Test
    void getComments() {
        assertNotNull(itemBookingDtoAll.getNextBooking());
    }

    @Test
    void setId() {
        itemBookingDto.setId(3L);
        assertEquals(3L, itemBookingDto.getId());
    }

    @Test
    void setName() {
        itemBookingDto.setName("Name");
        assertEquals("Name", itemBookingDto.getName());
    }

    @Test
    void setDescription() {
        itemBookingDto.setDescription("Description123");
        assertEquals("Description123", itemBookingDto.getDescription());
    }

    @Test
    void setAvailable() {
        itemBookingDto.setAvailable(false);
        assertEquals(false, itemBookingDto.getAvailable());
    }

    @Test
    void setLastBooking() {
        itemBookingDtoAll.setLastBooking(new BookingDtoReturn(4L,5L));
        assertEquals(4L, itemBookingDtoAll.getLastBooking().getId());
    }

    @Test
    void setNextBooking() {
        itemBookingDtoAll.setNextBooking(new BookingDtoReturn(777L,5L));
        assertEquals(777L, itemBookingDtoAll.getNextBooking().getId());
    }

    @Test
    void setComments() {
        itemBookingDtoAll.setComments(new ArrayList<>());
        assertNotNull(itemBookingDtoAll.getNextBooking());
    }

    @Test
    void testEmptyConstructor() {

        ItemBookingDto empty1 = new ItemBookingDto();
        assertNotNull(empty1);
    }

    @Test
    void testAllArgsConstructor() {
        ItemBookingDto itemBookingDtoAll1 = new ItemBookingDto(2L, "itemBookingDto", "ItemBookingDtoDescription", true, lastBooking, nextBooking, comments);
        assertNotNull(itemBookingDtoAll1);
    }

    @Test
    void testPartAllArgsConstructor() {
        ItemBookingDto itemBookingDto1 = new ItemBookingDto(1L, "itemBookingDto", "ItemBookingDtoDescription", true);
        assertNotNull(itemBookingDto1);
    }
}