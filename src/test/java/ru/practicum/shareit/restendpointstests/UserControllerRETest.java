package ru.practicum.shareit.restendpointstests;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(UserController.class)
class UserControllerRETest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;



    Long userId = 0L;

    @SneakyThrows
    @Test
    void getUserById() {
        mockMvc.perform(get("/users/{id}", userId))
                .andDo(print())
                        .andExpect(status().isOk());

      verify(userService).getUserById(userId);
    }

}