package ru.practicum.shareit.restendpointstests;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerRETest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    Long userId = 0L;

    Long itemId = 1L;

    @SneakyThrows
    @Test
    void getItemById() {
        mockMvc.perform(get("/items/{itemId}",  itemId).header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).getItemById(userId, itemId);
    }

}