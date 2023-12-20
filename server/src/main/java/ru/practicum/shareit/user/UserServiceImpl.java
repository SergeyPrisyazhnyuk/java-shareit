package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotUniqueEmailException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user = userRepository.save(user);
        return UserMapper.toUserDtoWithId(user);
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto userDto) {

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Не найден юзер с id: " + id));

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            checkEmailUnique(user);
            user.setEmail(userDto.getEmail());
        }

        userRepository.save(user);
        return UserMapper.toUserDtoWithId(user);
    }

    @Override
    @Transactional
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Не найден юзер с id: " + id));
        return UserMapper.toUserDtoWithId(user);
    }

    @Override
    @Transactional
    public List<UserDto> getAll() {
        return userRepository
                .findAll()
                .stream()
                .map(UserMapper::toUserDtoWithId)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void checkEmailUnique(User user) {
        boolean notUniqueEmail = userRepository
                .findAll()
                .stream()
                .anyMatch(currentUser -> !currentUser.getId().equals(user.getId()) && currentUser.getEmail().equalsIgnoreCase(user.getEmail()));
        if (notUniqueEmail) {
            throw new NotUniqueEmailException("User with email " + user.getEmail() + " already exists.");
        }
    }

}
