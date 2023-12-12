package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    Long userId = 0L;

    @Test
    void save_whenInvoked_thenUserExists() {
        UserDto userDtoExpected = new UserDto();
        UserDto userDto = new UserDto();

        Mockito.when(userService.save(userDto)).thenReturn(userDtoExpected);

        UserDto user = userController.save(userDto);

        assertEquals(userDtoExpected, user);

    }

    @Test
    void update_whenInvoked_thenUserExists() {

        UserDto userDtoExpected = new UserDto();
        UserDto userDto = new UserDto();

        Mockito.when(userService.update(userId, userDto)).thenReturn(userDtoExpected);

        UserDto user = userController.update(userId, userDto);

        assertEquals(userDtoExpected, user);
    }

    @Test
    void getUserById_whenInvoked_thenUserExists() {

        UserDto userDtoExpected = new UserDto();

        Mockito.when(userService.getUserById(userId)).thenReturn(userDtoExpected);

        UserDto user = userController.getUserById(userId);

        assertEquals(userDtoExpected, user);
    }

    @Test
    void getAll_whenInvoked_thenUsersExists() {

        List<UserDto> userDtoExpected = List.of(new UserDto(), new UserDto(), new UserDto());

        Mockito.when(userService.getAll()).thenReturn(userDtoExpected);

        List<UserDto> user = userController.getAll();

        assertEquals(userDtoExpected, user);
        assertEquals(3, user.size());

    }
}