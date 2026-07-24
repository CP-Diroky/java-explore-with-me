package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.models.User;

import java.util.Collection;

@UtilityClass
public class UserMapper {

    public static User toUser(NewUserRequest userRequest) {
        return new User(
                userRequest.getEmail(),
                userRequest.getName()
        );
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getEmail(),
                user.getId(),
                user.getName()
        );
    }


    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public static Collection<UserDto> toUserDtoCollection(Collection<User> users) {
        return users.stream().map(UserMapper::toUserDto).toList();
    }

}
