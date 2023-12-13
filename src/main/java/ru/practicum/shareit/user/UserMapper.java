package ru.practicum.shareit.user;

public class UserMapper {


    public static UserDto toUserDtoWithId(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getName(),
                userDto.getEmail()
        );
    }

}
