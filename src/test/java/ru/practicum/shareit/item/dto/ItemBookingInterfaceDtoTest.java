package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class ItemBookingInterfaceDtoTest {

    @Autowired
    private JacksonTester<ItemBookingInterfaceDto> json;

    ItemBookingInterfaceDto itemBookingInterfaceDto = new ItemBookingInterfaceDto(1L, "InterfaceDto");

    @Test
    @SneakyThrows
    public void testId() {
        JsonContent<ItemBookingInterfaceDto> jsonContent = this.json.write(itemBookingInterfaceDto);
        jsonContent.assertThat().hasJsonPath("@.id", 1);
    }

    @Test
    @SneakyThrows
    public void testName() {
        JsonContent<ItemBookingInterfaceDto> jsonContent = this.json.write(itemBookingInterfaceDto);
        jsonContent.assertThat().hasJsonPathStringValue("@.name", "InterfaceDto");
    }

    @Test
    void testEmptyConstructor() {

        ItemBookingInterfaceDto empty1 = new ItemBookingInterfaceDto();
        assertNotNull(empty1);
    }

    @Test
    void testAllArgsConstructor() {
        ItemBookingInterfaceDto itemBookingDtoAll1 = new ItemBookingInterfaceDto(2L, "itemBookingDto");
        assertNotNull(itemBookingDtoAll1);
        assertEquals(2L, itemBookingDtoAll1.getId());
        assertEquals("itemBookingDto", itemBookingDtoAll1.getName());
    }


    @Test
    void getId() {
        assertNotNull(itemBookingInterfaceDto.getId());
    }

    @Test
    void getName() {
        assertNotNull(itemBookingInterfaceDto.getName());
    }

    @Test
    void setId() {
        itemBookingInterfaceDto.setId(2L);
        assertEquals(2L, itemBookingInterfaceDto.getId());
    }

    @Test
    void setName() {
        itemBookingInterfaceDto.setName("2_Name");
        assertEquals("2_Name",itemBookingInterfaceDto.getName());
    }
}