package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    public UserDto save(UserDto userDto) {
        return userStorage.save(userDto);
    };

    public UserDto update(Long id, UserDto userDto) {
        return userStorage.update(id, userDto);
    };

    public UserDto getUserById(Long id) {
        return userStorage.getUserById(id);
    };

    public List<UserDto> getAll() {
        return userStorage.getAll();
    };

    public void deleteUserById(Long id) {
        userStorage.deleteUserById(id);
    };

    public void deleteAll() {
        userStorage.deleteAll();
    };
}
