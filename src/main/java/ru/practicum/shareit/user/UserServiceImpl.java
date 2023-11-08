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

    @Override
    public UserDto save(UserDto userDto) {
        return userStorage.save(userDto);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        return userStorage.update(id, userDto);
    }

    @Override
    public UserDto getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    @Override
    public List<UserDto> getAll() {
        return userStorage.getAll();
    }

    @Override
    public void deleteUserById(Long id) {
        userStorage.deleteUserById(id);
    }

    @Override
    public void deleteAll() {
        userStorage.deleteAll();
    }
}
