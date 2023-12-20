package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {


    User user = new User(1L, "username", "user@email.ru");
    User user2 = new User("username", "user@email.ru");

    UserDto userDtoE = new UserDto();

    UserDto userDtoId = new UserDto(5L);

    UserDto userDto = new UserDto(8L, "userNameDto", "userDto@email.ru");


    @Test
    void toUserDtoWithId() {
        UserDto userDto1 = UserMapper.toUserDtoWithId(user);

        user2.setId(3L);
        UserDto userDto2 = UserMapper.toUserDtoWithId(user2);

        assertEquals(1L, userDto1.getId());
        assertEquals(3L, userDto2.getId());
    }

    @Test
    void toUser() {
        User user1 = UserMapper.toUser(userDto);
        User user3 = UserMapper.toUser(userDtoE);
        User user4 = UserMapper.toUser(userDtoId);

        assertNull(user1.getId());
        assertEquals("userNameDto", user1.getName());
        assertNull(user3.getId());
        assertNull(user3.getName());
        assertNull(user4.getId());
        assertNull(user4.getName());

    }
}