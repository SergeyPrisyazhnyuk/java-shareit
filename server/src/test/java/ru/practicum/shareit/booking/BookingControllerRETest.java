package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.item.dto.ItemBookingInterfaceDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerRETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    Long userId = 0L;
    Long bookingId = 0L;

    String state = "ALL";
    String fromS = "0";
    String sizeS = "10";
    Integer from = 0;
    Integer size = 10;

    LocalDateTime start = LocalDateTime.now().plusMinutes(2);
    LocalDateTime end = LocalDateTime.now().plusMinutes(10);

    @SneakyThrows
    @Test
    void save_whenInvoked_thenBookingReturns()  {

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(3L);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);

        BookingDtoReturn bookingDtoReturn = new BookingDtoReturn();
        ItemBookingInterfaceDto itemBookingInterfaceDto = new ItemBookingInterfaceDto();
        itemBookingInterfaceDto.setId(3L);
        bookingDtoReturn.setItem(itemBookingInterfaceDto);
        bookingDtoReturn.setStart(start);
        bookingDtoReturn.setEnd(end);

        when(bookingService.save(userId, bookingDto)).thenReturn(bookingDtoReturn);


        String result = mockMvc.perform(post("/bookings", bookingDto)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoReturn), result);
    }

/*
    @SneakyThrows
    @Test
    void save_whenInvoked_thenBookingReturnsBadRequest()  {

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(3L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(end);

        BookingDtoReturn bookingDtoReturn = new BookingDtoReturn();
        ItemBookingInterfaceDto itemBookingInterfaceDto = new ItemBookingInterfaceDto();
        itemBookingInterfaceDto.setId(3L);
        bookingDtoReturn.setItem(itemBookingInterfaceDto);
        bookingDtoReturn.setStart(start);
        bookingDtoReturn.setEnd(end);

        when(bookingService.save(userId, bookingDto)).thenReturn(bookingDtoReturn);


        mockMvc.perform(post("/bookings", bookingDto)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

            verify(bookingService, never()).save(userId, bookingDto);
    }
*/

    @SneakyThrows
    @Test
    void update_whenInvoked_thenBookingReturns()  {

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(3L);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);

        BookingDtoReturn bookingDtoReturn = new BookingDtoReturn();
        ItemBookingInterfaceDto itemBookingInterfaceDto = new ItemBookingInterfaceDto();
        itemBookingInterfaceDto.setId(3L);
        bookingDtoReturn.setItem(itemBookingInterfaceDto);
        bookingDtoReturn.setStart(start);
        bookingDtoReturn.setEnd(end);

        when(bookingService.updateBookingStatus(userId, bookingId, true)).thenReturn(bookingDtoReturn);


        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoReturn), result);
    }

    @SneakyThrows
    @Test
    void getAll_whenInvoked_thenBookingReturns()  {
        mockMvc.perform(get("/bookings", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", fromS)
                        .param("size", sizeS))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).getAll(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void getAllByOwner_whenInvoked_thenBookingReturns()  {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", fromS)
                        .param("size", sizeS))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).getAllByOwner(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void getBookingByUserId_whenInvoked_thenBookingReturns()  {
        mockMvc.perform(get("/bookings/{bookingId}", bookingId).header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).getBookingByUserId(userId, bookingId);
    }
}