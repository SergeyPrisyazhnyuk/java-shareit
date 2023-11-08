package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotUniqueEmailException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserStorageInMemory implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    public Long setIdReturn() {
        return id++;
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        checkEmailUnique(user);
        Long id = setIdReturn();
        user.setId(id);
        users.put(id, user);
        userDto.setId(id);
        return userDto;

    }

    @Override
    public UserDto update(Long id, UserDto userDto) {

        User user = Optional.ofNullable(users.get(id)).orElseThrow(() -> new NotFoundException("Не найден юзер с id: " + id));

        User userToCheck = new User();
        userToCheck.setId(id);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
            userToCheck.setName(userDto.getName());
        } else {
            userDto.setName(user.getName());
            userToCheck.setName(user.getName());

        }

        if (userDto.getEmail() != null) {
            userToCheck.setEmail(userDto.getEmail());
            checkEmailUnique(userToCheck);
            user.setEmail(userDto.getEmail());
        } else {
            userDto.setEmail(user.getEmail());
            userToCheck.setEmail(user.getEmail());
        }

        users.put(id, user);


        userDto.setId(id);
        return userDto;

    }


    @Override
    public UserDto getUserById(Long id) {
            User user = Optional.ofNullable(users.get(id)).orElseThrow(() -> new NotFoundException("Не найден юзер с id: " + id));
            UserDto userDto = UserMapper.toUserDtoWithId(user);
            userDto.setId(id);
            return userDto;
    }

    @Override
    public List<UserDto> getAll() {
        return users
                .values()
                .stream()
                .map(UserMapper::toUserDtoWithId)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void deleteUserById(Long id) {
        if (users.get(id) != null) {
            users.remove(id);
        } else {
            throw new NotFoundException("Не найден юзер с id: " + id);
        }

    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    public void checkEmailUnique(User user) {
        boolean notUniqueEmail = users
                .values()
                .stream()
                .anyMatch(currentUser -> !currentUser.getId().equals(user.getId()) && currentUser.getEmail().equalsIgnoreCase(user.getEmail()));
        if (notUniqueEmail) {
            throw new NotUniqueEmailException("User with email " + user.getEmail() + " already exists.");
        }
    }

    public Map<Long, User> getUsers() {

        Map<Long, User> userMap = users;

        return userMap;
    }

}
