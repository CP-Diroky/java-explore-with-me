package ru.practicum.controllers.Admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.mappers.UserMapper;
import ru.practicum.services.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/admin/users")
@Validated
public class AdminUserController {


    private final UserService userService;


    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid NewUserRequest userRequest) {
        return UserMapper.toUserDto(userService.addUser(UserMapper.toUser(userRequest)));
    }

    @GetMapping
    public Collection<UserDto> getUsers(@RequestParam(required = false) Collection<Long> ids,
                                        @RequestParam(defaultValue = "10") @Positive int size,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero int from) {
        return UserMapper.toUserDtoCollection(userService.getUsers(ids, size, from));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(name = "userId") @Positive Long id) {
        userService.deleteUser(id);
    }
}
