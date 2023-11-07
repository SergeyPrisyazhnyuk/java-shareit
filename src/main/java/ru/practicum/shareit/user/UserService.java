package ru.practicum.shareit.user;

import java.util.List;


public interface UserService {

    UserDto save(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    UserDto getUserById(Long id);

    List<UserDto> getAll();

    void deleteUserById(Long id);

    void deleteAll();
}
