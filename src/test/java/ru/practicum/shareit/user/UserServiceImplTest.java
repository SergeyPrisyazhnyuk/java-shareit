package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotUniqueEmailException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;


    UserDto userDto = new UserDto(1L, "user", "user@yandex.ru");
    User user = UserMapper.toUser(userDto);



    @Test
    void save_whenInvoked_ThenOk() {
        when(userRepository.save(user)).thenReturn(user);

        UserDto userDtoResult = userService.save(userDto);
        userDtoResult.setId(1L);

        assertEquals(userDto, userDtoResult);
    }

    @Test
    void update_whenInvoked_ThenOk() {
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserDto userDtoResult = userService.update(userDto.getId(), userDto);

        assertNotNull(userDtoResult);
    }

    @Test
    void update_whenInvoked_ThenNotFoundException() {
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> userService.update(2L, userDto));

        assertEquals(notFoundException.getMessage(), "Не найден юзер с id: " + 2L);
    }

    @Test
    void update_whenInvoked_NotUniqueEmail() {
        User user2 = new User(2L, "user", "user@yandex.ru");
        user.setId(1L);
        when(userRepository.findAll()).thenReturn(List.of(user, user2));


        NotUniqueEmailException notUniqueEmailException = assertThrows(NotUniqueEmailException.class,
                () -> userService.checkEmailUnique(user));

        assertEquals(notUniqueEmailException.getMessage(), "User with email " + user.getEmail() + " already exists.");
    }

    @Test
    void getUserById_whenInvoked_ThenOk() {
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));

        UserDto userDtoResult = userService.getUserById(1L);
        assertNotNull(userDtoResult);
    }

    @Test
    void getUserById_whenInvoked_ThenNotFoundException() {
       NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> userService.getUserById(2L));

        assertEquals(notFoundException.getMessage(), "Не найден юзер с id: " + 2L);
    }


    @Test
    void getAll_whenInvoked_ThenOk() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> userDtos = userService.getAll();

        assertFalse(userDtos.isEmpty());
    }

    @Test
    void deleteUserById_whenInvoked_ThenOk() {
        userService.deleteUserById(1L);
    }
}