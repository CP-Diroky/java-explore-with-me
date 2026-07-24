package ru.practicum.services;

import ru.practicum.models.User;

import java.util.Collection;

public interface UserService {

    User addUser(User user);

    Collection<User> getUsers(Collection<Long> ids, int size, int from);

    void deleteUser(Long id);
}
